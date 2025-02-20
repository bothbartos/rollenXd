
export default function SongComment({ comment }) {
    return (
        <div className="flex items-start space-x-4 p-4 m-3 bg-gray-800 rounded-lg shadow-lg">
            <img
                src={`data:image/jpeg;base64,${comment.profilePicture}`}
                alt="Profile Picture"
                className="w-12 h-12 rounded-full object-cover"
            />
            <div>
                <p className="text-gray-300 font-semibold">{comment.text}</p>
                <h4 className="text-white ">{comment.username}</h4>
            </div>
        </div>

    )
}