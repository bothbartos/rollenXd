
export default function SongComment({ comment }) {
    return (
        <div className="flex-grow">
            <h1 className="text-white">{comment.text}</h1>
            <h1 className="text-white">{comment.username}</h1>
            <img
                src={`data:image/jpeg;base64,${comment.profilePicture}`}
                alt="profilePicture"
            />
        </div>
    )
}