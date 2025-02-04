import axios from "axios";
import AudioPlayer from "../components/AudioPlayer.jsx";
import {useQuery} from "@tanstack/react-query";

async function getSong(songTitle) {
    return await axios.get(`/api/song/title/${songTitle}`);
}

export default function PlaySong() {
    const {data, error, isLoading} = useQuery({
        query: ["song"],
        queryFn: getSong
    })

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return(
        <AudioPlayer song={data}/>
    )
}
