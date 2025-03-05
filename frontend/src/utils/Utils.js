import {useContext} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";

export const handlePlaySong = async (e, song) => {
    const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;
    const {setCurrentSong, setHistory} = useContext(PlayerContext);

    e.preventDefault();
    const songToPlay = {
        title: song.title,
        author: song.author,
        audioSrc: `${STREAMING_BASE_URL}/api/song/stream/${encodeURIComponent(song.id)}`,
        coverSrc: song.coverBase64 ? `data:image/png;base64,${song.coverBase64}` : './cover.png',
        id: song.id,
        numberOfLikes: song.numberOfLikes,
        reShares: song.reShares
    }
    setCurrentSong(songToPlay);
    setHistory((state) => [...state, songToPlay])
};
