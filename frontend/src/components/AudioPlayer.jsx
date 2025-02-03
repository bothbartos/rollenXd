export default function AudioPlayer({song}) {
    const {title, audioBase64, length, numberOfLikes, reShare} = song;

    if (song === undefined) {
        return <p>Loading...</p>
    }

    return (
        <div>
            <h3>Song: {title}</h3>
            <p>Length: {length} seconds</p>
            <p>Likes: {numberOfLikes}</p>
            <p>Re-shares: {reShare}</p>

            <audio controls>
                <source src={`data:audio/mp3;base64,${audioBase64}`} type="audio/mp3"/>
                Your browser does not support the audio element.
            </audio>
        </div>
    )
}
