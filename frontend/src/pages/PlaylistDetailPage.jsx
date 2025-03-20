import axiosInstance from "../context/AxiosInstance.jsx";
import {useParams} from "react-router-dom";
import {usePlayerActions} from "../hooks/UsePlayerActions.js";
import {useQuery} from "@tanstack/react-query";
import {convertDoubleToMinuteSecond} from "../utils/Utils.js";
import Comments from "../components/Comments.jsx";
import UserDetailsSong from "../components/UserDetailsSongs.jsx";
import PlaylistSongs from "../components/PlaylistSongs.jsx";


async function getPlaylistById(id) {
    const response = await axiosInstance.get(`/api/playlist/id/${encodeURIComponent(id)}`);
    return response.data;
}

export default function PlaylistDetailPage() {
    const {id} = useParams();
    const {playPlaylist} = usePlayerActions();

    const {data, isLoading, error} = useQuery({
        queryKey: ["playlistId", id],
        queryFn: () => getPlaylistById(id),
    })

    const handlePlay = (e) => {
        e.preventDefault();
        e.stopPropagation();
        playPlaylist(data)
    }

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="flex w-full justify-center pt-[88px]">
            <div className="flex items-start">
                <div
                    className="relative w-100 h-100 flex-shrink-0 group"
                >
                    <img
                        className="w-full h-full object-cover rounded-lg shadow-md"
                        src={`data:image/png;base64,${data.songs[0].coverBase64}`}
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

                <div className="ml-4 flex flex-col items-center">
                    <h1 className="text-white text-lg font-medium m-3">
                        {`Creator: ${data?.author} `}
                    </h1>
                    <h1 className="text-white text-lg font-medium m-3">
                        {`Title: ${data?.title}`}
                    </h1>

                </div>
            </div>

            <div className="flex flex-col w-1/3 bg-gray-700 rounded-lg shadow-md p-4">
                <h2 className="text-xl font-semibold text-sky-500 mb-4">Songs</h2>
                <div className="space-y-4 overflow-y-auto h-[300px] scrollbar-hide">
                    {
                        data.songs.map((song, index) => (
                            <PlaylistSongs song={song} key={index}/>
                        ))
                    }
                </div>
            </div>

        </div>
    )
}