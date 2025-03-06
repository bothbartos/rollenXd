import {useContext} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";

export function usePlayerActions() {
    const { setCurrentSong, setHistory } = useContext(PlayerContext);
    const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;

    function convertSong(song){
        return {
            title: song.title,
            author: song.author,
            audioSrc: `${STREAMING_BASE_URL}/api/song/stream/${encodeURIComponent(song.id)}`,
            coverSrc: song.coverBase64 ? `data:image/png;base64,${song.coverBase64}` : './cover.png',
            id: song.id,
            numberOfLikes: song.numberOfLikes,
            reShares: song.reShares,
        };
    }

    const playSong = async (song) => {
        const songToPlay = convertSong(song);
        setCurrentSong(songToPlay);
        setHistory((state) => [...state, songToPlay])
    };

    const playPlaylist = async (playlist) => {
        const formattedPlaylist = playlist.songs.map((song) => {
            return convertSong(song);
        });
        setHistory((state) => [...state, ...formattedPlaylist]);
        setCurrentSong(formattedPlaylist[0]);
    }

    return {playSong, playPlaylist};

}