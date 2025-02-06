import {useState} from 'react';
import {useNavigate, Outlet} from 'react-router-dom';

export default function NavBar() {
    const [searchString, setSearchString] = useState('');
    const navigate = useNavigate();

    const handleSearch = (e) => {
        e.preventDefault()
        if (searchString.trim()) {
            navigate(`/search?query=${encodeURIComponent(searchString)}`);
        }
    };

    const handleUpload = (e) => {
        e.preventDefault();
        navigate("/upload-song");
    }

    return (
        <div className="navbar">
            <nav>
                <ul>
                    <li>
                        <button id={"upload"} onClick={handleUpload}>Upload</button>
                    </li>
                    <li>
                        <input
                            id="searchBar"
                            value={searchString}
                            onChange={(e) => setSearchString(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && handleSearch(e)}
                        />
                        <button
                            type="button"
                            id="searchButton"
                            onClick={e => handleSearch(e)}
                        >
                            Search
                        </button>
                    </li>
                </ul>
            </nav>
            <Outlet />
        </div>
    )

}
