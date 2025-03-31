import {useQuery} from '@tanstack/react-query';
import axios from 'axios';
import MediaElement from "./components/MediaElement.jsx";
import {useContext} from "react";
import {PlayerContext} from "./context/PlayerContext.jsx";
import axiosInstance from "./context/AxiosInstance.jsx";

async function getAllSongs() {
    const response = await axiosInstance.get(`/api/song/all`);
    return response.data
}

async function getAllPlaylists() {
    const response = await axiosInstance.get(`/api/playlist/all`);
    return response.data;

}

export default function App() {
    const {currentSong} = useContext(PlayerContext)

    const {data: songs, error: songError, isLoading: isLoadingSong} = useQuery({
            queryKey: ["songs"],
            queryFn: getAllSongs,
        }
    )

    const {data: playlists, error: playlistError, isLoading: isLoadingPlaylist} = useQuery({
        queryKey: ["playlists"],
        queryFn: getAllPlaylists,
    })


    if (isLoadingSong || isLoadingPlaylist) return <p>Loading...</p>;
    if (playlistError || songError) return <p>Error: {playlistError?.message || songError?.message}</p>;

    return (
        <div className={`h-full p-4 space-y-8 pt-[88px] ${currentSong ? 'pb-32' : ''}`}>
            {/* Songs Section */}
            <div className="w-full">
                <h1 className="text-2xl font-semibold mb-1 ml-10 text-white">Songs:</h1>
                <div className="w-full overflow-x-auto scrollbar-hide">
                    <div className="flex flex-nowrap pl-4">
                        {songs?.map((song) => (
                            <div
                                key={song.id}
                                className="flex-shrink-0 mr-4 last:mr-0 m-10"
                            >
                                <MediaElement item={song} type="song" />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <hr className="my-12 h-0.5 border-t-0 bg-neutral-100 dark:bg-white/10" />
            {/* Playlists Section */}
            <div className="w-full">
                <h1 className="text-2xl font-semibold mb-1 ml-10 text-white">Playlists:</h1>
                <div className="w-full overflow-x-auto scrollbar-hide">
                    <div className="flex flex-nowrap pl-4">
                        {playlists?.map((playlist) => (
                            <div
                                key={playlist.id}
                                className="flex-shrink-0 mr-4 last:mr-0 m-10"
                            >
                                <MediaElement item={playlist} type="playlist" />
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );

}

