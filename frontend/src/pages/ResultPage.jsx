import { useSearchParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import SongListElement from "../components/MediaElement.jsx";
import MediaElement from "../components/MediaElement.jsx";

async function searchSongs(searchString) {
    return await axios.get(`/api/song/search?search=${encodeURIComponent(searchString)}`);
}

export default function ResultPage() {
    const [searchParams] = useSearchParams();
    const searchQuery = searchParams.get('query') || '';

    const { data, error, isLoading } = useQuery({
        queryKey: ['searchSongs', searchQuery],
        queryFn: () => searchSongs(searchQuery),
        enabled: !!searchQuery
    });

    if (isLoading) return <p>Searching songs...</p>;
    if (error) return <p>Error: {error.message}</p>;
    if (!data?.data?.length) return <p>No results found</p>;
    data.data.forEach((song) => {
        console.log(song.coverBase64)
    })

    return (
        <div className="w-full overflow-x-auto scrollbar-hide">
            <div className="flex flex-nowrap pl-4">
                {data.data.map((song) => (
                    <div key={song.title} className="flex-shrink-0 mr-4 last:mr-0 m-10">
                        <MediaElement
                            item={song}
                            type="song"
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}