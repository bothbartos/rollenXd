import {useNavigate} from "react-router-dom";

export default function SongListElement({ songTitle, songAuthor }) {
    const navigate = useNavigate();

    const handleClick = (e) => {
        e.preventDefault()
        navigate(`/play-song?songTitle=${songTitle}`)
    }

    return (
        <div onClick={handleClick}>
            <li>{songTitle}</li>
            <li>{songAuthor}</li>
        </div>
    )
}
