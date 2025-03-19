import {useState} from "react";
import axiosInstance from "../context/AxiosInstance.jsx";
import PlaylistSongSelector from "../components/PlaylistSongSelector.jsx";
import {useNavigate} from "react-router-dom";

async function uploadPlaylist({title, songId}){
    await axiosInstance.post(`/api/playlist/upload`, {title, songId});
}

export default function CreatePlaylistForm() {
    const [title, setTitle] = useState('');
    const [songId, setSongId] = useState([]);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        await uploadPlaylist({title, songId});
        navigate("/");
    }

    const handleClick = (id, isClicked) =>{
        if(!isClicked){
            setSongId([...songId, id]);
        }else{
            setSongId([...songId.filter(songId => songId !== id)]);
        }
    }
    console.log(songId)

    return (
        <div className="flex justify-center items-center mt-15 text-white">
            <div className="bg-gray-800 p-6 rounded-lg shadow-lg w-96">
                <h2 className="text-2xl font-semibold text-center mb-4">Create Playlist</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input
                        type="text"
                        value={title}
                        onChange={e => setTitle(e.target.value)}
                        placeholder="Playlist title"
                        className="w-full px-4 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <PlaylistSongSelector handleClick={handleClick} />
                    <button
                        type="submit"
                        className="w-full py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition duration-300"
                    >
                        Create Playlist
                    </button>
                </form>
            </div>
        </div>
    );
}