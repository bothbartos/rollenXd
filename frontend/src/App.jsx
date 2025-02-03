import {useQuery} from '@tanstack/react-query';
import axios from 'axios';
import './App.css'
import AudioPlayer from "./components/AudioPlayer.jsx";

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

    return(
        <div>
            {data.data.map((song) => {
                return <AudioPlayer song={song} key={song.title} />
            })}
        </div>
    )
}

