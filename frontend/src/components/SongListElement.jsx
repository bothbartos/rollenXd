import {useNavigate} from "react-router-dom";
import {useState} from "react";

export default function SongListElement({ songTitle, songAuthor }) {
    const navigate = useNavigate();
    const [isLiked, setIsLiked] = useState(false);

    const handleClick = (e) => {
        e.preventDefault()
        navigate(`/play-song?songTitle=${songTitle}`)
    }

    return (
        <div className="flex flex-col items-center w-full max-w-xs mx-auto">
            <div className="relative w-full aspect-square mb-2">
                <img
                    className="w-full h-full object-cover rounded-lg shadow-md"
                    alt="Song Cover"
                    src="/cover.png"
                    onClick={handleClick}
                />
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
                    {songAuthor + " - " + songTitle}
                </a>
            </div>
        </div>
    )

}
