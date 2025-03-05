import {useQuery} from '@tanstack/react-query';
import axios from 'axios';
import SongListElement from "./components/MediaElement.jsx";
import PlayListElement from "./components/PlayListElement.jsx";
import MediaElement from "./components/MediaElement.jsx";

async function getAllSongs() {
    return await axios.get("/api/song/all", {withCredentials: true});
}

async function getAllPlaylists() {
    return await axios.get("/api/playlist/all", {withCredentials: true});

}

export default function App() {
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
    console.log(playlists);

    return (
        <div>
            <div className="w-full overflow-x-auto scrollbar-hide">
                <div className="flex flex-nowrap pl-4">
                    {songs.data.map((song) => (
                        <div key={song.title} className="flex-shrink-0 mr-4 last:mr-0 m-10">
                            <MediaElement
                                item={song}
                                type="song"
                            />
                        </div>
                    ))}
                </div>
            </div>
                <div className="w-full overflow-x-auto scrollbar-hide">
                    <div className="flex flex-nowrap pl-4">
                        {playlists.data.map((playlist) => (
                            <div key={playlist.title} className="flex-shrink-0 mr-4 last:mr-0 m-10">
                                <MediaElement item={playlist}
                                type="playlist"/>
                            </div>
                        ))}
                    </div>
                </div>
        </div>
    );

}

