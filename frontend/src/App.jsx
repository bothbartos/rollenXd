import {useQuery} from '@tanstack/react-query';
import axios from 'axios';
import './App.css'
import SongListElement from "./components/SongListElement.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

async function getAllSongs() {
    return await axios.get("/api/song/all", {withCredentials: true});
}

export default function App() {
    const navigate = useNavigate();
    const {data, error, isLoading} = useQuery({
            query: ["songs"],
            queryFn: getAllSongs,
        }
    )

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/login");
        }
    }, [navigate]);


    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <div className="w-full overflow-x-auto scrollbar-hide">
            <div className="flex flex-nowrap pl-4">
                    {data.data.map((song) => (
                        <div key={song.title} className="flex-shrink-0 mr-4 last:mr-0">
                            <SongListElement
                                songTitle={song.title}
                                songAuthor={song.author}
                                songCover={song.coverBase64}
                            />
                        </div>
                    ))}
                </div>
            </div>
    );

}

