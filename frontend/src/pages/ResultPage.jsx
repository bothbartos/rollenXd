import { useSearchParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import SongListElement from "./SongListElement.jsx";

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

    return (
        <div className="search-results">
            {data.data.map((song) => (
                <SongListElement key={song.title} song={song} />
            ))}
        </div>
    );
}
