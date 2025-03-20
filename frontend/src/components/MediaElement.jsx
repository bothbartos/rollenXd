import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {usePlayerActions} from "../hooks/UsePlayerActions.js";
import axios from "axios";
import axiosInstance from "../context/AxiosInstance.jsx";

async function getPlaylist(id) {
    const response = await axiosInstance.get(`/api/playlist/id/${id}`);
    return response.data;
}

async function likeSong(id) {
    const response = await axiosInstance.post(`/api/song/like/id/${id}`);
    return response.data;
}

async function unlikeSong(id) {
    const response = await axiosInstance.delete(`/api/song/unlike/id/${id}`);
    return response.data;
}

export default function MediaElement({ item, type }) {

    if (type === 'song') {
        console.log(item.isLiked)
    }

    const [isLiked, setIsLiked] = useState(type === "song" ? item.isLiked : false);
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

    const handleLike = (e) => {
        e.preventDefault();
        if (type === 'song'){
            if (isLiked){
                unlikeSong(item.id).then((res) => console.log(res));
            } else {
                likeSong(item.id).then((res) => console.log(res));
            }
        }
    }

    const handlePlay = async (e) => {
        e.preventDefault();
        e.stopPropagation();
        if(type === 'song') {
            playSong(item)
        }else{
            const playlist = await getPlaylist(item.id);
            playPlaylist(playlist)
        }
    }


    const coverImage = item.coverBase64;
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
                            handleLike(e)
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
