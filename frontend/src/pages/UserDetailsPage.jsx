import axios from "axios";
import axiosInstance from "../context/AxiosInstance.jsx";
import {useQuery} from "@tanstack/react-query";
import {convertDoubleToMinuteSecond} from "../utils/Utils.js";


async function getUserDetails() {
    const res = await axiosInstance.get(`/api/user/details`)
    return res.data
}

export default function UserDetailPage() {

    const {data, isLoading, error} = useQuery({
        queryKey: ["userDetails"],
        queryFn: getUserDetails,
    })

    if (isLoading) {
        return <p>Loading...</p>
    }
    if (error) {
        return <p>Error: {error?.message}</p>;
    }

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
                </div>
            </div>

            {/* Songs Section */}
            <div className="flex flex-col w-1/3 bg-gray-700 rounded-lg shadow-md p-4">
                <h2 className="text-xl font-semibold text-sky-500 mb-4">Songs</h2>
                <div className="space-y-4 overflow-y-auto h-[300px]">
                    {data.songs && data.songs.length > 0 ? (
                        data.songs.map((song, index) => (
                            <div
                                key={index}
                                className="flex items-center space-x-4 bg-gray-800 p-3 rounded-md"
                            >
                                <img
                                    src={`data:image/png;base64,${song.coverBase64}`}
                                    alt={song.title}
                                    className="w-12 h-12 object-cover rounded-md"
                                />
                                <div>
                                    <h3 className="font-medium text-gray-200">{song.title}</h3>
                                    <p className="text-sm text-gray-400">{convertDoubleToMinuteSecond(song.length)}</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p className="text-gray-400">No uploaded songs</p>
                    )}
                </div>
            </div>

            {/* Playlists Section */}
            <div className="flex flex-col w-1/3 bg-gray-700 rounded-lg shadow-md p-4">
                <h2 className="text-xl font-semibold text-sky-500 mb-4">Playlists</h2>
                <div className="space-y-4 overflow-y-auto h-[300px]">
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