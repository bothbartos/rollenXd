import {useState} from 'react';
import {useNavigate, Outlet, Link} from 'react-router-dom';

export default function NavBar() {
    const [searchString, setSearchString] = useState('');
    const navigate = useNavigate();

    const handleSearch = (e) => {
        e.preventDefault()
        if (searchString.trim()) {
            navigate(`/search?query=${encodeURIComponent(searchString)}`);
        }
    };

    return (
        <>
            <nav className="fixed top-0 left-0 right-0 z-50 bg-white shadow dark:bg-gray-800">
                <div className="container px-6 py-3 mx-auto">
                    <div className="flex flex-col md:flex-row md:justify-between md:items-center">
                        <div className="flex items-center justify-between">
                            <div className="flex items-center">
                                <Link to="/" className="text-2xl font-bold text-gray-800 dark:text-white">
                                    <img src={"public/icon.svg"} className="h-15 w-15 mr-2" alt="Logo"/>
                                </Link>
                                <div className="hidden mx-10 md:block">
                                    <div className="relative">
                                <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                                    <svg className="w-5 h-5 text-gray-400" viewBox="0 0 24 24" fill="none">
                                        <path
                                            d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z"
                                            stroke="currentColor" strokeWidth="2" strokeLinecap="round"
                                            strokeLinejoin="round"></path>
                                    </svg>
                                </span>
                                        <input
                                            type="text"
                                            className="w-full py-2 pl-10 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300"
                                            placeholder="Search"
                                            value={searchString}
                                            onChange={(e) => setSearchString(e.target.value)}
                                            onKeyDown={(e) => e.key === 'Enter' && handleSearch(e)}
                                        />
                                    </div>
                                </div>
                                <div className="flex items-center justify-between">
                                    <Link to={"/upload-song"}>
                                        <button id={"upload"}>Upload</button>
                                    </Link>
                                </div>
                            </div>
                        </div>
                        <div className="my-4 md:hidden">
                            <div className="relative">
                        <span className="absolute inset-y-0 left-0 flex items-center pl-3">
                            <svg className="w-5 h-5 text-gray-400" viewBox="0 0 24 24" fill="none">
                                <path
                                    d="M21 21L15 15M17 10C17 13.866 13.866 17 10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10Z"
                                    stroke="currentColor" strokeWidth="2" strokeLinecap="round"
                                    strokeLinejoin="round"></path>
                            </svg>
                        </span>
                                <input
                                    type="text"
                                    className="w-full py-2 pl-10 pr-4 text-gray-700 bg-white border rounded-md dark:bg-gray-800 dark:text-gray-300 dark:border-gray-600 focus:border-blue-400 dark:focus:border-blue-300 focus:outline-none focus:ring focus:ring-opacity-40 focus:ring-blue-300"
                                    placeholder="Search"
                                    value={searchString}
                                    onChange={(e) => setSearchString(e.target.value)}
                                    onKeyDown={(e) => e.key === 'Enter' && handleSearch(e)}
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </nav>
            <div className="pt-[88px]">
                <Outlet/>
            </div>
        </>
    );

}
