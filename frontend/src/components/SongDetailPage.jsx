import axios from "axios";
import {useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";

async function fetchDetails(id) {
    return await axios.get(`/api/song/id/${encodeURIComponent(id)}`)
}

const SongDetailPage = () => {
    const {id} = useParams();

    const {data, error, isLoading} = useQuery({
        queryKey: ['id', id],
        queryFn: ()=> fetchDetails(id),
        enabled: true
    })
    if (isLoading)  return <>Loading...</>
    if(error) return <p>{error}</p>

    return (
        <>
            <h1>
                {data.data.title}
            </h1>
        </>
    )
}

export default SongDetailPage