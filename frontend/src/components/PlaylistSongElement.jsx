import {convertDoubleToMinuteSecond} from "../utils/Utils.js";
import {useContext, useEffect, useState} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";
import {usePlayerActions} from "../hooks/UsePlayerActions.js";

export default function PlaylistSongElement({ song }) {
    const {currentSong} = useContext(PlayerContext);
    const {playSong} = usePlayerActions();
    const [divColor, setDivColor] = useState("bg-gray-800")
    useEffect(() => {
        if (currentSong && currentSong.id === song.id) {
            setDivColor("bg-blue-700")
        }else{
            setDivColor("bg-gray-800")
        }
    }, [currentSong])

    const handlePlay = (e) => {
        e.preventDefault();
        e.stopPropagation()
        playSong(song)
    }

    return (
        <div className={`flex items-center justify-between ${divColor} bg-blue p-3 rounded-md hover:bg-gray-600`}
            onClick={(e) => handlePlay(e)}>
            <div className="flex items-center space-x-4">
                <img
                    src={`data:image/png;base64,${song.coverBase64}`}
                    alt={song.title}
                    className="w-12 h-12 object-cover rounded-md"
                />
                <div>
                    <h3 className="font-medium text-gray-200">{song.title}</h3>
                    <p className="text-sm text-gray-400">{convertDoubleToMinuteSecond(song.length)}</p>
                </div>
            </div>
        </div>
    );

}