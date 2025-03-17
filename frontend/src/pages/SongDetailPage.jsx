import axios from "axios";
import {useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import {useContext} from "react";
import {PlayerContext} from "../context/PlayerContext.jsx";
import Comments from "../components/Comments.jsx";
import {usePlayerActions} from "../hooks/UsePlayerActions.js";


async function fetchDetails(id) {
    const response = await axios.get(`/api/song/id/${encodeURIComponent(id)}`)
    return response.data
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
    const {playSong} = usePlayerActions();

    const {data, isLoading, error} = useQuery({
        queryKey:["songId", id],
        queryFn: ()=> fetchDetails(id),
    })

    const handlePlay = (e) =>{
        e.preventDefault();
        e.stopPropagation()
        playSong(data);
    }




    if (isLoading ) return <p>Loading...</p>
    if (error) return <p>{error}</p>


    return (
        <div className="flex w-full justify-center pt-10">
            <div className="flex items-start">
                <div
                    className="relative w-100 h-100 flex-shrink-0 group"
                >
                    <img
                        className="w-full h-full object-cover rounded-lg shadow-md"
                        src={`data:image/png;base64,${data?.coverBase64}`}
                        alt="cover_image"
                    />

                    <div
                        className="absolute inset-0 bg-gray-500 bg-opacity-40 rounded-lg opacity-0 group-hover:opacity-40 transition-opacity duration-300"></div>

                    <div
                        className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                        <div className="w-12 h-12 bg-white rounded-full flex items-center justify-center cursor-pointer"
                             onClick={handlePlay}>
                            <img src="/play_arrow.svg" alt="Play" className="w-6 h-6"/>
                        </div>
                    </div>
                </div>

                <div className="ml-4 flex flex-col items-center">
                    <h1 className="text-white text-lg font-medium m-3">
                        {`Artist: ${data?.author} `}
                    </h1>
                    <h1 className="text-white text-lg font-medium m-3">
                        {`Title: ${data?.title}`}
                    </h1>

                    <p className="text-white text-sm opacity-80 m-3">
                        {`Length: ${convertDoubleToMinuteSecond(data?.length)}`}
                    </p>

                    <div className="ml-4 flex space-x-6 m-2">
                        <h1 className="text-white text-sm">{`Likes: ${data?.numberOfLikes}`}</h1>
                        <h1 className="text-white text-sm">{`Reshares: ${data?.reShares}`}</h1>
                    </div>
                </div>
            </div>

            <Comments
            songId={id}/>

        </div>
    );

}

export default SongDetailPage