import AudioPlayer from "../components/AudioPlayer.jsx";
import {useSearchParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import axios from "axios";

async function getSong(title){
    return await axios.get(`/api/song/title/${title}`);
}

export default function PlaySongPage() {
    const [searchParams] = useSearchParams();
    const songTitle = searchParams.get("songTitle") || "";

    const { data, error, isLoading } = useQuery({
        queryKey: ['searchSongs', songTitle],
        queryFn: () => getSong(songTitle),
        enabled: !!songTitle
    })

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <div className="PlaySongPage">
            <AudioPlayer song={data.data}/>
        </div>
    )
}
