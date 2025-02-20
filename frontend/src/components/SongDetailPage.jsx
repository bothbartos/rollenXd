import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {useQueries, useQuery} from "@tanstack/react-query";
import {useContext, useEffect, useState} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";
import SongComment from "./SongComment.jsx";

const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;

async function fetchDetails(id) {
    const response = await axios.get(`/api/song/id/${encodeURIComponent(id)}`)
    return response.data
}

async function fetchComments(id) {
    const response =await axios.get(`/api/comment/id/${encodeURIComponent(id)}`)
    return response.data;
}

function convertDoubleToMinuteSecond(seconds) {
    let minutes = Math.floor(seconds / 60);
    let newSeconds = Math.floor(seconds - minutes * 60);
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (newSeconds < 10) {
        newSeconds = "0" + seconds;
    }
    return `${minutes}:${newSeconds}`;
}

const SongDetailPage = () => {
    const {id} = useParams();

    const {setCurrentSong} = useContext(PlayerContext);

    const [authorId, setAuthorId] = useState("")
    const [text, setText] = useState("")
    const navigate = useNavigate()

    const results = useQueries({
        queries: [
            {
                queryKey: ['songDetails', id],
                queryFn: () => fetchDetails(id),
            },
            {
                queryKey: ['songComments', id],
                queryFn: () => fetchComments(id),
            },
        ]
    });




    const [songDetails, songComments] = results;
    if (songDetails.isLoading && songComments.isLoading) return <>Loading...</>
    if (songDetails.error && songComments.error) return <p>{songDetails.error}</p>

    const handlePlay = async (e) => {
        e.preventDefault();
        setCurrentSong({
            title: songDetails.data.title,
            author: songDetails.data.author,
            audioSrc: `${STREAMING_BASE_URL}/api/song/stream/${encodeURIComponent(songDetails.data.id)}`,
            coverSrc: songDetails.data?.coverBase64 ? `data:image/png;base64,${songDetails.data.coverBase64}` : './cover.png',
            id: songDetails.data.id,
            numberOfLikes: songDetails.data.numberOfLikes,
            reShares: songDetails.data.reShares
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem("token");

        if (!token) {
            alert("You must be logged in to comment.");
            return;
        }

        const payload = token.split(".")[1];
        const decodedPayload = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
        setAuthorId(decodedPayload.sub);

        console.log(decodedPayload.sub);
        console.log(authorId)
        const formData = new FormData();
        formData.append("songId", id);
        formData.append("user", decodedPayload.sub);
        formData.append("text", text);

        try{
           await axios.post("/api/comment/addComment", formData,{
               headers: {
                   'Content-Type': 'multipart/form-data'}
           })
        }catch(error){
            console.log(error)
            alert("Commenting failed:" + error.message)
        }
    }


    return (
        <div className="flex w-full justify-start pl-15 pt-10">
            <div className="flex items-start">
                <div
                    className="relative w-100 h-100 flex-shrink-0 group"
                >
                    <img
                        className="w-full h-full object-cover rounded-lg shadow-md"
                        src={`data:image/png;base64,${songDetails.data?.coverBase64}`}
                        alt="cover_image"
                    />

                    <div
                        className="absolute inset-0 bg-gray-500 bg-opacity-40 rounded-lg opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>

                    <div
                        className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                        <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center cursor-pointer"
                             onClick={handlePlay}>
                            <img src="/play_arrow.svg" alt="Play" className="w-6 h-6"/>
                        </div>
                    </div>
                </div>

                <div className="ml-4 flex flex-col items-start">
                    <h1 className="text-white text-lg font-medium">
                        {`${songDetails.data?.author} - ${songDetails.data?.title}`}
                    </h1>

                    <p className="text-white text-sm opacity-80">
                        {`Length: ${convertDoubleToMinuteSecond(songDetails.data?.length)}`}
                    </p>

                    <div className="flex space-x-6 mt-2">
                        <h1 className="text-white text-sm">{`Likes: ${songDetails.data?.numberOfLikes}`}</h1>
                        <h1 className="text-white text-sm">{`Reshares: ${songDetails.data?.reShares}`}</h1>
                    </div>
                </div>
            </div>

            <div className="flex-grow">
                {songComments.data ?
                    songComments.data.map(comment =>
                    <SongComment
                    comment={comment}
                    key={comment.id}/>
                ) : <></>}
                <div className="flex space-x-6 mt-2">
                    <form onSubmit={handleSubmit}>
                        <label htmlFor={text}>
                            Comment
                        </label>
                        <input
                        type="text"
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                        name="text"
                        placeholder="Write a comment..."/>

                    </form>
                </div>
            </div>
        </div>
    );

}

export default SongDetailPage