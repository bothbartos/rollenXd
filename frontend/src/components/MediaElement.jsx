import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import {usePlayerActions} from "../hooks/UsePlayerActions.js";

export default function MediaElement({ item, type }) {
    const [isLiked, setIsLiked] = useState(false);
    const navigate = useNavigate();
    const {playSong, playPlaylist} = usePlayerActions();

    const handleClick = (e) => {
        e.preventDefault();
        if (type === 'song') {
            navigate(`/songDetails/${encodeURIComponent(item.id)}`);
        } else if (type === 'playlist') {
            // Navigate to playlist details page (implement this route)
            navigate(`/playlistDetails/${encodeURIComponent(item.id)}`);
        }
    };

    const handlePlay = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if(type === 'song') {
            playSong(item)
        }else{
            playPlaylist(item)
        }
    }


    const coverImage = type === 'song' ? item.coverBase64 : item.songs[0]?.coverBase64;
    const title = type === 'song' ? `${item.author} - ${item.title}` : item.title;

    return (
        <div className="flex flex-col items-center w-[180px] min-w-[180px] max-w-[180px] mx-auto group cursor-pointer min-h-[250px]" onClick={handleClick}>
            <div className="relative w-[200px] h-[200px] overflow-hidden rounded-lg shadow-md">
                <img
                    className="w-full h-full object-cover"
                    alt={type === 'song' ? "Song Cover" : "Playlist Cover"}
                    src={`data:image/png;base64,${coverImage}`}
                />
                <div className="absolute inset-0 bg-gray-500 bg-opacity-40 opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>
                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <button
                        className="w-12 h-12 bg-white rounded-full flex items-center justify-center cursor-pointer"
                        onClick={handlePlay}
                    >
                        <img src="/play_arrow.svg" alt="Play" className="w-6 h-6" />
                    </button>
                </div>
                {type === 'song' && (
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
                )}
            </div>
            <div className="text-center w-full mt-2 min-h-[40px] flex items-center justify-center">
                <a className="text-sm font-medium text-gray-800 hover:text-gray-600 dark:text-gray-200 dark:hover:text-gray-400 cursor-pointer truncate w-full text-center">
                    {title}
                </a>
            </div>
        </div>
    );
}
