import axios from "axios";
import {useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import {useContext} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";

const STREAMING_BASE_URL = import.meta.env.VITE_STREAMING_BASE_URL;

async function fetchDetails(id) {
    return await axios.get(`/api/song/id/${encodeURIComponent(id)}`)
}

function convertDoubleToMinuteSecond(seconds) {
    let minutes = Math.floor(seconds / 60);
    let newSeconds = Math.floor(seconds - minutes * 60);
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (newSeconds < 10) {
        newSeconds = "0" + seconds;
    }
    return `${minutes}:${newSeconds}`;
}

const SongDetailPage = () => {
    const {id} = useParams();

    const {setCurrentSong} = useContext(PlayerContext);

    const {data, error, isLoading} = useQuery({
        queryKey: ['id', id],
        queryFn: () => fetchDetails(id),
        enabled: true
    })

    const song = data?.data;
    if (isLoading) return <>Loading...</>
    if (error) return <p>{error}</p>

    const handlePlay = async (e) => {
        e.preventDefault();
        setCurrentSong({
            title: song.title,
            author: song.author,
            audioSrc: `${STREAMING_BASE_URL}/api/song/stream/${encodeURIComponent(song.id)}`,
            coverSrc: song.coverBase64 ? `data:image/png;base64,${song.coverBase64}` : './cover.png',
            id: song.id,
            numberOfLikes: song.numberOfLikes,
            reShares: song.reShares
        });
    };


    return (
        <div className="flex w-full justify-start pl-15 pt-10">
            <div className="flex items-start">
                <div
                    className="relative w-100 h-100 flex-shrink-0 group"
                >
                    <img
                        className="w-full h-full object-cover rounded-lg shadow-md"
                        src={`data:image/png;base64,${data.data.coverBase64}`}
                        alt="cover_image"
                    />

                    <div className="absolute inset-0 bg-gray-500 bg-opacity-40 rounded-lg opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>

                    <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                        <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center cursor-pointer"
                        onClick={handlePlay}>
                            <img src="/play_arrow.svg" alt="Play" className="w-6 h-6" />
                        </div>
                    </div>
                </div>

                <div className="ml-4 flex flex-col items-start">
                    <h1 className="text-white text-lg font-medium">
                        {`${data.data.author} - ${data.data.title}`}
                    </h1>

                    <p className="text-white text-sm opacity-80">
                        {`Length: ${convertDoubleToMinuteSecond(data.data.length)}`}
                    </p>

                    <div className="flex space-x-6 mt-2">
                        <h1 className="text-white text-sm">{`Likes: ${data.data.numberOfLikes}`}</h1>
                        <h1 className="text-white text-sm">{`Reshares: ${data.data.reShares}`}</h1>
                    </div>
                </div>
            </div>

            <div className="flex-grow"></div>
        </div>
    );

}

export default SongDetailPage