import axios from "axios";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useState} from "react";
import SongComment from "./SongComment.jsx";


async function fetchComments(id) {
    const response = await axios.get(`/api/comment/id/${encodeURIComponent(id)}`)
    return response.data;
}

async function postComment({ songId, text }) {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("You must be logged in to comment.");
        return;
    }

    const formData = new FormData();
    formData.append("songId", songId);
    formData.append("text", text);

    try {
        return await axios.post("/api/comment/addComment", formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${token}`
            }
        })
    } catch (error) {
        console.log(error)
        alert("Commenting failed:" + error.message)
    }
}

export default function Comments({songId}) {
    const queryClient = useQueryClient();
    const [text, setText] = useState("");

    const {data: comments, isLoading, error} = useQuery({
        queryKey: ["comments", songId],
        queryFn: () => fetchComments(songId)
    });

    const mutation = useMutation({
        mutationFn: postComment,
        onMutate: async (newComment) => {
            await queryClient.cancelQueries(["comments", songId])
            const prevComment = queryClient.getQueryData(["comments", songId])

            queryClient.setQueryData(['comments', songId], (old)=> [
                ...old,
                {id: Math.random(), text: newComment.text, isOptimistic: true},
            ]);
            return {prevComment};
        },
        onSuccess: ()=>{
            queryClient.invalidateQueries(["comments", songId])
        },
        onError: (err, newContent, context)=>{
            queryClient.setQueryData(["comments", songId], context.prevComment)
        }
    });

    const handleCommentSubmit = (e) => {
        e.preventDefault();
        if(text.trim() === "") return;
        mutation.mutate({songId, text});
        setText("");
    }

    if (isLoading) return <>Loading...</>
    if (error) return <p>{error}</p>


    return(
        <div className="flex ml-4">
            {/* Comments Section */}
            <div className="pt-7 pl-7 pr-7 bg-gray-800 rounded-lg shadow-lg flex flex-col">
                <h2 className="text-white text-lg font-semibold mb-5">Comments</h2>

                {/* Scrollable Comments */}
                <div className="h-60 overflow-y-auto space-y-4">
                    {comments ? (
                        comments?.map((comment) => (
                            <SongComment comment={comment} key={comment.id}/>
                        ))
                    ) : (
                        <p className="text-gray-400">No comments yet.</p>
                    )}
                </div>

                {/* Comment Input Box */}
                <div className="mt-4">
                    <form onSubmit={handleCommentSubmit} className="flex items-center space-x-2">
                        <input
                            type="text"
                            value={text}
                            onChange={(e) => setText(e.target.value)}
                            name="text"
                            placeholder="Write a comment..."
                            className="flex-grow p-2 bg-gray-700 text-white rounded-lg outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <button
                            type="submit"
                            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
                        >
                            Send
                        </button>
                    </form>
                </div>
            </div>
        </div>
    )

}