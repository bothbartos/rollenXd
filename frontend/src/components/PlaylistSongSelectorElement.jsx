import {useState} from "react";


export default function PlaylistSongSelectorElement({song, handleClick}) {
    const [isClicked, setClicked] = useState(false);

    return (
        <div key={song.id} className="flex justify-between  w-full bg-gray-800 p-4 rounded-md shadow">
            <p className="text-white">{`${song.title} - ${song.author}`}</p>
            <button
                type="button"
                onClick={()=> {
                    handleClick(song.id, isClicked)
                    setClicked(!isClicked);
                }}
                className="bg-blue-600 text-white px-3 py-1 rounded-md hover:bg-blue-700 transition duration-300"
            >
                {isClicked? "-" : "+"}
            </button>
        </div>
    );


}