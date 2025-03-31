import {useContext} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";

export function usePlayerActions() {
    const { setCurrentSong, setHistory } = useContext(PlayerContext);

    function convertSong(song){
        return {
            title: song.title,
            author: song.author,
            audioSrc: `/api/song/stream/${encodeURIComponent(song.id)}`,
            coverSrc: song.coverBase64 ? `data:image/png;base64,${song.coverBase64}` : './cover.png',
            id: song.id,
            numberOfLikes: song.numberOfLikes,
            reShares: song.reShares,
        };
    }

    const playSong = (song) => {
        const songToPlay = convertSong(song);
        setCurrentSong(songToPlay);
        setHistory((state) => [...state, songToPlay])
    };

    const playPlaylist = (playlist) => {
        console.log(playlist);
        const formattedPlaylist = playlist.map((song) => {
            return convertSong(song);
        });
        setHistory((state) => [...state, ...formattedPlaylist]);
        setCurrentSong(formattedPlaylist[0]);
    }

    return {playSong, playPlaylist};
}