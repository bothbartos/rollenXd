import {useQuery} from '@tanstack/react-query';
import axios from 'axios';
import SongListElement from "./components/SongListElement.jsx";

async function getAllSongs() {
    return await axios.get("/api/song/all", {withCredentials: true});
}

export default function App() {
    const {data, error, isLoading} = useQuery({
            query: ["songs"],
            queryFn: getAllSongs,
        }
    )


    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <div className="w-full overflow-x-auto scrollbar-hide">
            <div className="flex flex-nowrap pl-4">
                    {data.data.map((song) => (
                        <div key={song.title} className="flex-shrink-0 mr-4 last:mr-0 m-10">
                            <SongListElement
                                song={song}
                            />
                        </div>
                    ))}
                </div>
            </div>
    );

}

