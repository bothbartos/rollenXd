import {convertDoubleToMinuteSecond} from "../utils/Utils.js";
import React, {useContext, useEffect, useState} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";


export default function UserDetailsSong({ song, handleDelete, handlePlay }) {
    const [divColor, setDivColor] = useState("bg-gray-800")
    const {currentSong} = useContext(PlayerContext);
    useEffect(() => {
        if (currentSong && currentSong.id === song.id) {
            setDivColor("bg-blue-700")
        }else{
            setDivColor("bg-gray-800")
        }
    }, [currentSong])

    return (
        <div className={`flex items-center justify-between ${divColor} p-3 rounded-md hover:bg-gray-600`}
        onClick={(e)=>handlePlay(e, song)}>
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
            <img
                className="w-6 h-6 cursor-pointer"
                src="/delete_icon.svg"
                alt="Delete icon"
                onClick={(e)=>handleDelete(e, song.id)}
            />
        </div>
    );

}