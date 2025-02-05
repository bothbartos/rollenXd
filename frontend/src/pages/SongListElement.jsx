export default function SongListElement({ song }) {
    const {title, author, length } = song;
    return (
        <div>
            <li>{title}</li>
            <li>{author}</li>
        </div>
    )
}