import axios from "axios";
import {useQuery} from "@tanstack/react-query";
import PlaylistSongSelectorElement from "./PlaylistSongSelectorElement.jsx";

async function fetchAllSongs() {
    const response = await axios.get(`/api/song/all`);
    return response.data
}

export default function PlaylistSongSelector({handleClick}) {
    const {data, error, isLoading} = useQuery({
        queryKey: ["songs"],
        queryFn: fetchAllSongs,
    })

    if(isLoading) return <div>Loading...</div>
    if(error) return <div>{error.message}</div>

    return (
        <div className="flex flex-col space-y-4 bg-gray-900 text-white p-6 rounded-lg mx-auto">
            {data?.map((song) => (
                <PlaylistSongSelectorElement song={song} handleClick={handleClick}/>
            ))}
        </div>
    );

}