import {useContext, useState} from 'react';
import {useNavigate, Outlet, Link} from 'react-router-dom';
import SongPlayer from "./SongPlayer.jsx";
import {PlayerContext} from "../context/PlayerContext.jsx";
import {useQuery} from "@tanstack/react-query";
import axiosInstance from "../context/AxiosInstance.jsx";

async function getUserDetails() {
    const response = await axiosInstance.get("/api/user/details")
    return response.data;
}

export default function NavBar() {
    const [searchString, setSearchString] = useState('');
    const navigate = useNavigate();
    const {currentSong, setCurrentSong} = useContext(PlayerContext);
    const token = localStorage.getItem('token');

    const [showSearch, setShowSearch] = useState(false);
    const [showLeftDropdown, setShowLeftDropdown] = useState(false);
    const [showRightDropdown, setShowRightDropdown] = useState(false);

    const handleSearch = (e) => {
        e.preventDefault()
        if (searchString.trim()) {
            navigate(`/search?query=${encodeURIComponent(searchString)}`);
        }
        setSearchString("")
    };

    const {data, error, isLoading} = useQuery({
        queryKey: ["userDetails"],
        queryFn: getUserDetails,
        enabled: !!token
    })

    const logout = (e) => {
        e.preventDefault();
        localStorage.removeItem("token")
        navigate("/login");
        setCurrentSong(null)
    }

    const handleNavigate = () => {
        navigate("/login")
    }
    if (isLoading) return <a>Loading..</a>
    if (error) return <Outlet/>

    return (
        <>
            <nav
                className="flex justify-between items-center bg-gray-800 text-white px-6 py-4 shadow-md fixed top-0 left-0 right-0 z-50">
                {/* Left Dropdown */}
                {token && (

                <div className="relative">
                    <button
                        onClick={() => setShowLeftDropdown(!showLeftDropdown)}
                        className="btn btn-text btn-circle bg-gray-700 hover:bg-gray-600 p-2 rounded-full cursor-pointer"
                        aria-haspopup="menu"
                        aria-expanded={showLeftDropdown}
                        aria-label="Dropdown"
                    >
                        <div className="w-[20px] h-[20px] rounded-full overflow-hidden">
                            <img src="/oreo-menu.svg" alt="oreo-menu"/>
                        </div>
                    </button>
                    {showLeftDropdown && (
                        <ul
                            className="absolute left-0 mt-2 w-48 bg-gray-700 rounded-lg shadow-lg p-2"
                            role="menu"
                        >
                            <li onClick={() => setShowLeftDropdown(false)}>
                                <Link to="/upload-song"
                                      className="block text-sm text-white hover:bg-gray-600 rounded-md px-4 py-2">
                                    Upload Song
                                </Link>
                            </li>
                            <li onClick={() => setShowLeftDropdown(false)}>
                                <Link to="/create-playlist"
                                      className="block text-sm text-white hover:bg-gray-600 rounded-md px-4 py-2">
                                    Create Playlist
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>
                )}

                {/* Center Logo */}
                <div className="flex items-center space-x-2">
                    <img src="/icon.svg" alt="Logo" className="h-8 w-8"/>
                    <Link to="/" className="text-xl font-semibold hover:text-sky-400">
                        RollenXd
                    </Link>
                </div>

                {/* Right Section */}
                <div className="flex items-center space-x-4">
                    {/* Search Button */}
                    {token && (
                        <div className="relative w-[300px]">
                            <input
                                type="text"
                                className="w-full py-2 pl-10 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300"
                                placeholder="Search for songs..."
                                value={searchString}
                                onChange={(e) => setSearchString(e.target.value)}
                                onKeyDown={(e) => e.key === "Enter" && handleSearch(e)}
                            />
                            <span className="absolute inset-y-0 left-0 flex items-center pl-3 cursor-pointer">
                            <img src="/search-icon.svg" alt="Search" className="w-[20px] h-[20px]"
                                 onClick={(e) => handleSearch(e)}/>
                        </span>
                        </div>
                    )}
                    {/* Profile Dropdown */}
                    {token && data && (

                        <div className="relative">
                            <button
                                onClick={() => setShowRightDropdown(!showRightDropdown)}
                                className="flex items-center cursor-pointer"
                                aria-haspopup="menu"
                                aria-expanded={showRightDropdown}
                                aria-label="Profile Dropdown"
                            >
                                <div className="avatar">
                                    <div className="w-[38px] h-[38px] rounded-full overflow-hidden">
                                        <img src={`data:image/png;base64,${data.profileImageBase64}`} alt="Profile"/>
                                    </div>
                                </div>
                            </button>
                            {showRightDropdown && (
                                <ul
                                    className="absolute right-0 mt-2 w-[150px] bg-gray-700 rounded-lg shadow-lg p-2"
                                    role="menu"
                                >
                                    <li onClick={() => setShowRightDropdown(false)}>
                                        <Link to="/user-detail"
                                              className="block text-sm text-white hover:bg-gray-600 rounded-md px-4 py-2">
                                            My Profile
                                        </Link>
                                    </li>
                                    <li onClick={() => setShowRightDropdown(false)}>
                                        <button
                                            onClick={token ? logout : handleNavigate}
                                            className="block text-sm text-red-500 hover:bg-red-600 hover:text-red-200 rounded-md px-4 py-2 w-full text-left"
                                        >
                                            {token ? "Logout" : "Login"}
                                        </button>
                                    </li>
                                </ul>
                            )}
                        </div>
                    )}
                </div>
            </nav>

            <div>
                <Outlet/>
            </div>
            {currentSong && <SongPlayer/>}
        </>
    );


}
