import {convertDoubleToMinuteSecond} from "../utils/Utils.js";

export default function PlaylistSongs({ song }) {

    return (
        <div className={`flex items-center justify-between bg-gray-800 p-3 rounded-md`}>
            <div className="flex items-center space-x-4">
                <img
                    src={`data:image/png;base64,${song.coverBase64}`}
                    alt={song.title}
                    className="w-12 h-12 object-cover rounded-md"
                />
                <div>
                    <h3 className="font-medium text-gray-200">{song.title}</h3>
                    <p className="text-sm text-gray-400">{convertDoubleToMinuteSecond(song.length)}</p>
                </div>
            </div>
        </div>
    );

}