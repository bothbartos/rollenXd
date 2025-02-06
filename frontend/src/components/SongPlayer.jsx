import { useState } from 'react';
import AudioPlayer from "react-modern-audio-player";
const SongPlayer = ({ song }) => {
    const [isPlaying, setIsPlaying] = useState(false);

    const playList = [{
        name: song.title,
        src: `data:audio/mp3;base64,${song.audioBase64}`,
        img: "/cover.png",
        id: 1,
    }];


    return (
        <div className="song-player">
            <AudioPlayer
                playList={playList}
                audioInitialState={{
                    curPlayId: 1,
                    playing: isPlaying,
                }}
                placement={{
                    player : "bottom"
                }}
                activeUI={{
                    all: true,
                    progress: "waveform",
                }}
                onPlayPauseChange={(playing) => setIsPlaying(playing)}
            />
            <div className="song-stats">
                <span>Likes: {song.numberOfLikes}</span>
                <span>Shares: {song.reShares}</span>
            </div>
        </div>
    );
};

export default SongPlayer;
