import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import axiosInstance from "../context/AxiosInstance.jsx";

export default function UploadSongForm() {
    const [title, setTitle] = useState('');
    const [audio, setAudio] = useState(null);
    const [cover, setCover] = useState(null);
    const navigate = useNavigate();

    function handleAudioChange(e) {
        setAudio(e.target.files[0]);
    }

    function handleCoverChange(e) {
        setCover(e.target.files[0]);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        if(!audio || !title){
            alert("Please fill in all fields.");
            return;
        }
        const formData = new FormData();
        formData.append("file", audio)
        formData.append("title", title)
        formData.append("cover", cover)

        try{
            await axiosInstance.post("/api/song/upload", formData, {
                headers: { "Content-Type": "multipart/form-data"}
            })
            navigate("/")
        } catch (error){
            console.error(error);
            alert("Upload Failed!" + error.message);
        }
    }

    return (
        <div className="flex flex-col items-center justify-center w-full max-w-md mx-auto">
            <form onSubmit={handleSubmit} className="w-full space-y-4">
                <div>
                    <label className="block text-sm font-medium text-sky-600 dark:text-sky-300" htmlFor="title">
                        Title
                    </label>
                    <input
                        className="w-full py-2 pl-10 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300"
                        id="title"
                        name="title"
                        type="text"
                        onChange={(e) => setTitle(e.target.value)}
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-sky-600 dark:text-sky-300" htmlFor="audio">
                        Song
                    </label>
                    <input
                        className="w-full py-2 pl-4 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-sky-500 dark:file:bg-gray-700 dark:file:text-gray-200"
                        id="audio"
                        name="audio"
                        type="file"
                        accept="audio/*"
                        onChange={handleAudioChange}
                    />
                </div>
                <div>
                <label className="block text-sm font-medium text-sky-600 dark:text-sky-300" htmlFor="audio">
                    Cover
                </label>
                <input
                    className="w-full py-2 pl-4 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-sky-500 dark:file:bg-gray-700 dark:file:text-gray-200"
                    id="cover"
                    name="cover"
                    type="file"
                    accept="image/*"
                    onChange={handleCoverChange}
                />
            </div>

                <button
                    className="w-full rounded-md bg-sky-500 px-4 py-2 text-sm font-medium text-white hover:bg-sky-600 focus:outline-none focus:ring-2 focus:ring-sky-500 focus:ring-offset-2 dark:bg-sky-400 dark:text-sky-900 dark:hover:bg-sky-300 dark:focus:ring-sky-400"
                    type="submit"
                >
                    Submit
                </button>
            </form>
        </div>
    )

}
