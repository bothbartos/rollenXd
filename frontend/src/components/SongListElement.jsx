import {useContext, useState} from "react";
import {PlayerContext} from '../context/PlayerContext';
import {useNavigate} from "react-router-dom";
const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;

export default function SongListElement({ song }) {
    const [isLiked, setIsLiked] = useState(false);
    const { currentSong, setCurrentSong,setHistory } = useContext(PlayerContext);
    const navigate = useNavigate();

    const handlePlay = async (e) => {
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

    const handleClick = async (e) => {
        e.preventDefault();
        navigate(`/songDetails/${encodeURIComponent(song.id)}`);
    }


    return (
        <div className="flex flex-col items-center w-[180px] min-w-[180px] max-w-[180px] mx-auto group cursor-pointer min-h-[250px]" onClick={handleClick}>
            {/* Fixed-Size Container for Image */}
            <div className="relative w-[200px] h-[200px] overflow-hidden rounded-lg shadow-md">
                {/* Cover Image */}
                <img
                    className="w-full h-full object-cover"
                    alt="Song Cover"
                    src={`data:image/png;base64,${song.coverBase64}`}
                />

                {/* Semi-transparent Overlay on Hover */}
                <div className="absolute inset-0 bg-gray-500 bg-opacity-40 opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>

                {/* Play Button (Only Triggers handlePlay) */}
                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
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
                        e.stopPropagation();
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

            {/* Fixed Height & Width for Text */}
            <div className="text-center w-full mt-2 min-h-[40px] flex items-center justify-center">
                <a className="text-sm font-medium text-gray-800 hover:text-gray-600 dark:text-gray-200 dark:hover:text-gray-400 cursor-pointer truncate w-full text-center"
                   onClick={handleClick}>
                    {song.author + " - " + song.title}
                </a>
            </div>
        </div>
    );



}
