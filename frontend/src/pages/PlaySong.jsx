import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import AudioPlayer from "../components/AudioPlayer.jsx";

async function getSong(songTitle) {
    return await axios.get(`/api/song/title/${encodeURIComponent(songTitle)}`);
}

export default function PlaySong({ songTitle }) {
    const { data, error, isLoading } = useQuery({
        queryKey: ['song', songTitle],
        queryFn: () => getSong(songTitle)
    });

    if (isLoading) return <p>Loading song...</p>;
    if (error) return <p>Error loading song: {error.message}</p>;

    return <AudioPlayer song={data.data} />;
}
