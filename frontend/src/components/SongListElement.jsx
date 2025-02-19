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
        <div className="flex flex-col items-center w-full max-w-xs mx-auto group cursor-pointer" onClick={handleClick}>
            {/* Song Cover with Hover Effect */}
            <div className="relative w-full aspect-square mb-2">
                {/* Cover Image */}
                <img
                    className="w-full h-full object-cover rounded-lg shadow-md"
                    alt="Song Cover"
                    src={`data:image/png;base64,${song.coverBase64}`}
                />

                {/* Semi-transparent Overlay on Hover */}
                <div className="absolute inset-0 bg-gray-500 bg-opacity-40 rounded-lg opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>

                {/* Play Button (Only Triggers handlePlay) */}
                <div
                    className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                >
                    <button
                        className="w-12 h-12 bg-white rounded-full flex items-center justify-center cursor-pointer"
                        onClick={(e) => {
                            e.stopPropagation(); // Prevent triggering handleClick when clicking the play button
                            handlePlay(e);
                        }}
                    >
                        <img src="/play_arrow.svg" alt="Play" className="w-6 h-6" />
                    </button>
                </div>

                {/* Like Button */}
                <button
                    className="absolute top-2 right-2 p-2 bg-white bg-opacity-50 rounded-full hover:bg-opacity-75 transition-all duration-200"
                    onClick={(e) => {
                        e.stopPropagation(); // Prevent triggering handleClick when clicking the like button
                        setIsLiked(!isLiked);
                    }}
                >
                    <img
                        src={isLiked ? "/red-heart.svg" : "/heart-empty.svg"}
                        alt={isLiked ? "Liked" : "Not liked"}
                        className="w-6 h-6"
                    />
                </button>
            </div>

            {/* Song Title & Author */}
            <div className="text-center">
                <a className="text-sm font-medium text-gray-800 hover:text-gray-600 dark:text-gray-200 dark:hover:text-gray-400 cursor-pointer"
                onClick={handleClick}>
                    {song.author + " - " + song.title}
                </a>
            </div>
        </div>
    );



}
