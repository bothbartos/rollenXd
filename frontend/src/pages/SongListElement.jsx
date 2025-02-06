import {useNavigate} from "react-router-dom";

export default function SongListElement({ songTitle, songAuthor }) {
    const navigate = useNavigate();

    const handleClick = (e) => {
        e.preventDefault()
        navigate(`/play-song?songTitle=${songTitle}`)
    }

    return (
        <div onClick={handleClick} className="flex flex-col items-center w-full max-w-xs mx-auto">
            <div className="w-full aspect-square mb-2">
                <img className="w-full h-full object-cover rounded-lg shadow-md"
                     alt="Song Cover"
                     src={"/cover.png"}/>
            </div>
            <div className="text-center">
                <a className="text-sm font-medium text-gray-800 hover:text-gray-600 dark:text-gray-200 dark:hover:text-gray-400">
                    {songAuthor + " - " + songTitle}
                </a>
            </div>
        </div>
    )
}
