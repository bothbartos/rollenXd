import {useContext, useState} from "react";
import {PlayerContext} from '../context/PlayerContext';
import {useNavigate} from "react-router-dom";
const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;

export default function SongListElement({ song }) {
    const [isLiked, setIsLiked] = useState(false);
    const { setCurrentSong } = useContext(PlayerContext);
    const navigate = useNavigate();

    const handlePlay = async (e) => {
        e.preventDefault();
        setCurrentSong({
            title: song.title,
            author: song.author,
            audioSrc: `${STREAMING_BASE_URL}/api/song/stream/${encodeURIComponent(song.id)}`,
            coverSrc: song.coverBase64 ? `data:image/png;base64,${song.coverBase64}` : './cover.png',
            id: song.id,
            numberOfLikes: song.numberOfLikes,
            reShares: song.reShares
        });
    };

    const handleClick = async (e) => {
        e.preventDefault();
        navigate(`/songDetails/${encodeURIComponent(song.id)}`);
    }


    return (
        <div className="flex flex-col items-center w-full max-w-xs mx-auto">
            <div className="relative w-full aspect-square mb-2" >
                <img
                    className="w-full h-full object-cover rounded-lg shadow-md"
                    alt="Song Cover"
                    src={`data:image/png;base64,${song.coverBase64}`}
                    onClick={handleClick}
                />
                <button
                    className="absolute top-2 left-2 !right-auto p-2 bg-white bg-opacity-50 rounded-full hover:bg-opacity-75 transition-all duration-200"
                    onClick={handlePlay}
                >
                    <img
                        src="/play_arrow.svg"
                        alt="play arrow"
                        className="w-6 h-6"
                    />
                </button>
                <button
                    className="absolute top-2 right-2 p-2 bg-white bg-opacity-50 rounded-full hover:bg-opacity-75 transition-all duration-200"
                    onClick={() => setIsLiked(!isLiked)}
                >
                    <img
                        src={isLiked ? "/red-heart.svg" : "/heart-empty.svg"}
                        alt={isLiked ? "Liked" : "Not liked"}
                        className="w-6 h-6"
                    />
                </button>
            </div>
            <div className="text-center">
                <a className="text-sm font-medium text-gray-800 hover:text-gray-600 dark:text-gray-200 dark:hover:text-gray-400">
                    {song.author + " - " + song.title}
                </a>
            </div>
        </div>
    );



}
