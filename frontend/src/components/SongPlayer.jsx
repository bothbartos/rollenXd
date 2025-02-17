import {useContext, useEffect, useState} from 'react';
import AudioPlayer from "react-modern-audio-player";
import {PlayerContext} from "../context/PlayerContext.jsx";
const SongPlayer = () => {
    const [isPlaying, setIsPlaying] = useState(false);
    const { currentSong } = useContext(PlayerContext);

    const playList = [{
        name: currentSong.title,
        src: `data:audio/mp3;base64,${currentSong.audioBase64}`,
        img: `data:image/png;base64, ${currentSong.coverBase64}`,
        id: 1,
    }];

    useEffect(()=>{
        if(currentSong){
            setIsPlaying(true);
        }
    }, [currentSong]);

    if(!currentSong){
        return null;
    }


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
                <span>Likes: {currentSong.numberOfLikes}</span>
                <span>Shares: {currentSong.reShares}</span>
            </div>
        </div>
    );
};

export default SongPlayer;
