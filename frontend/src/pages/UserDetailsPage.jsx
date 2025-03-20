import axiosInstance from "../context/AxiosInstance.jsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import UserDetailsSong from "../components/UserDetailsSongs.jsx";
import {useState} from "react";
import UserDetailsEditForm from "./UserDetailsEditForm.jsx";


async function getUserDetails() {
    const res = await axiosInstance.get(`/api/user/details`)
    return res.data
}

async function deleteSongById(id){
    return await axiosInstance.delete(`/api/song/delete/id/${id}`);
}

async function updateUserDetails(formData) {
    const response = await axiosInstance.patch("/api/user/details/update", formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    });
    return response.data; // Return the updated user details
}


export default function UserDetailPage() {
    const queryClient = useQueryClient();
    const [isEditing, setEditing] = useState(false);

    const {data, isLoading, error} = useQuery({
        queryKey: ["userDetails"],
        queryFn: getUserDetails,
    })

    const deleteSongMutation = useMutation({
        mutationFn: deleteSongById,
        onMutate: async (deletedSongId) => {
            await queryClient.cancelQueries(["userDetails"]);

            const previousUserDetails = queryClient.getQueryData(["userDetails"]);

            queryClient.setQueryData(["userDetails"], (oldData) => ({
                ...oldData,
                songs: oldData.songs.filter((song) => song.id !== deletedSongId),
            }));

            return { previousUserDetails };
        },
        onSuccess: () => {
            queryClient.invalidateQueries(["userDetails"]);
        },
        onError: (err, deletedSongId, context) => {
            queryClient.setQueryData(["userDetails"], context.previousUserDetails);
        },
    });

    const updateProfileMutation = useMutation({
        mutationFn: updateUserDetails,
        onMutate: async (updatedData) => {
            await queryClient.cancelQueries(["userDetails"]);

            const previousUserDetails = queryClient.getQueryData(["userDetails"]);

            queryClient.setQueryData(["userDetails"], (oldData) => ({
                ...oldData,
                bio: updatedData.bio || oldData.bio,
                profileImageBase64: updatedData.profilePicture || oldData.profileImageBase64,
            }));

            return { previousUserDetails };
        },
        onSuccess: () => {
            queryClient.invalidateQueries(["userDetails"]);
        },
        onError: (err, updatedData, context) => {
            queryClient.setQueryData(["userDetails"], context.previousUserDetails);
        },
    });


    const handleEdit = (e) =>{
        e.preventDefault();
        setEditing(!isEditing);
    }

    function handleSubmit(e, bio, profilePicture) {
        e.preventDefault();
        if(bio === null){
            alert("Please enter a bio");
        } else{
            const formData = new FormData();
            formData.append("bio", bio);
            if (profilePicture) {
                formData.append("profilePicture", profilePicture);
            }

            updateProfileMutation.mutate(formData, {
                onSuccess: () => {
                    setEditing(false);
                },
                onError: (error) => {
                    console.error(error);
                    alert("Upload Failed! " + error.message);
                },
            });
        }
    }

    const handleDelete = (e, id) =>{
        e.preventDefault()
        if(confirm("Are you sure?")){
            deleteSongMutation.mutate(id)
        }
    }

    if(isLoading) return <p>Loading...</p>
    if(error) return <p>Error: {error.message}</p>


    return (
        <div className="flex flex-row items-start justify-center w-full h-full space-x-6 p-4">
            {/* User Details */}
            <div className="flex flex-col items-center w-1/4 bg-white dark:bg-gray-700 rounded-lg shadow-md p-4">
                <div className="text-center space-y-4">
                    <img
                        src={`data:image/png;base64,${data.profileImageBase64}`}
                        alt={data.name}
                        className="w-24 h-24 object-cover rounded-full mx-auto"
                    />
                    <h1 className="text-2xl font-bold text-sky-600 dark:text-sky-300">{data.name}</h1>
                    <p className="text-gray-600 dark:text-gray-400">{data.email}</p>
                    <p className="text-sm text-gray-500 dark:text-gray-500">
                        {data.bio ? data.bio : "No Bio"}
                    </p>
                    {!isEditing ? (
                    <button
                        className="w-full rounded-md bg-sky-500 px-4 py-2 text-sm font-medium text-white hover:bg-sky-600 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:ring-offset-2 dark:bg-sky-400 dark:text-sky-900 dark:hover:bg-sky-300 dark:focus:ring-sky-400"
                        type="button"
                        onClick={handleEdit}
                    >
                        Edit
                    </button>

                    ): <UserDetailsEditForm
                        oldBio={data.bio}
                        handleSubmit={handleSubmit}
                        handleCancel={handleEdit}
                    />}
                </div>
            </div>

            {/* Songs Section */}
            <div className="flex flex-col w-1/3 bg-gray-700 rounded-lg shadow-md p-4">
                <h2 className="text-xl font-semibold text-sky-500 mb-4">Songs</h2>
                <div className="space-y-4 overflow-y-auto h-[300px] scrollbar-hide">
                    {data.songs && data.songs.length > 0 ? (
                        data.songs.map((song, index) => (
                            <UserDetailsSong song = {song} key={index} handleDelete={handleDelete}/>
                        ))
                    ) : (
                        <p className="text-gray-400">No uploaded songs</p>
                    )}
                </div>
            </div>

            {/* Playlists Section */}
            <div className="flex flex-col w-1/3 bg-gray-700 rounded-lg shadow-md p-4">
                <h2 className="text-xl font-semibold text-sky-500 mb-4">Playlists</h2>
                <div className="space-y-4 overflow-y-auto h-[300px] scrollbar-hide">
                    {data.playlists && data.playlists.length > 0 ? (
                        data.playlists.map((playlist, index) => (
                            <div
                                key={index}
                                className="bg-gray-800 p-3 rounded-md"
                            >
                                <h3 className="font-medium text-gray-200">{playlist.title}</h3>
                            </div>
                        ))
                    ) : (
                        <p className="text-gray-400">No playlists</p>
                    )}
                </div>
            </div>
        </div>
    );
}