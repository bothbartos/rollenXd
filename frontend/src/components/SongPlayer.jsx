import {useContext, useEffect, useState} from 'react';
import AudioPlayer from "react-modern-audio-player";
import {PlayerContext} from "../context/PlayerContext.jsx";
const SongPlayer = () => {
    const [isPlaying, setIsPlaying] = useState(false);
    const { currentSong } = useContext(PlayerContext);

    const playList = [{
        name: currentSong.title,
        src: currentSong.audioSrc,
        img: currentSong.coverSrc,
        id: currentSong.id
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
                key={String(currentSong.id)}
                playList={playList}
                audioInitialState={{
                    curPlayId: currentSong.id,
                    playing: isPlaying,
                }}
                placement={{
                    player : "bottom"
                }}
                activeUI={{
                    all: true,
                    progress: "bar",
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

