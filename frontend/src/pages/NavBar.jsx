import {Outlet} from "react-router-dom";
import {useState} from "react";
import axios from "axios";

async function getSong(findBy, searchString) {
    return await axios.get(`/api/songs/${findBy}/${searchString}`);
}

export default function NavBar() {
    const [searchBy, setSearchBy] = useState("");


    return (
        <div className="navbar">
            <nav>
                <ul>
                    <li>
                        <select onChange={(e) => setSearchBy(e.target.value)}>
                            <option value="">Select...</option>
                            <option value={"title"}>Title</option>
                        </select>
                        <input id={"searchBar"}/>
                        <button type={"button"} id={"searchButton"}>Search</button>
                    </li>
                </ul>
            </nav>
            <Outlet />
        </div>
    )
}
