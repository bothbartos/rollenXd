import React, {useContext, useEffect, useState, useRef} from 'react';
import AudioPlayer, {RHAP_UI} from 'react-h5-audio-player';
import {PlayerContext} from "../context/PlayerContext.jsx";

const SongPlayer = () => {
    const {currentSong, setCurrentSong} = useContext(PlayerContext);
    const [isPlaying, setIsPlaying] = useState(false);
    const playerRef = useRef(null);
    const [currentSongIndex, setCurrentSongIndex] = useState(0);

    useEffect(() => {
        const audio = playerRef.current?.audio?.current;

        if (currentSong && audio) {
            audio.play().then(() => setIsPlaying(true)).catch(() => {});
            setCurrentSongIndex(0)
        }

    }, [currentSong]);

    const handleNext = () => {
        if(currentSong.length - 1=== currentSongIndex) {
            setCurrentSongIndex(0)
        }else{
            setCurrentSongIndex(currentSongIndex + 1)
        }
    }

    const handlePrevious = () => {
        if(currentSongIndex === 0){
            setCurrentSongIndex(0)
        }else {
            setCurrentSongIndex(currentSongIndex - 1)
        }
    }

    if (!currentSong) {
        return null;
    }

    return (
        <div className="fixed bottom-0 left-0 w-full bg-gray-800">
            <div className="max-w-screen-xl mx-auto px-4">
                <div className="song-stats py-2 text-sm text-gray-300">
                    <span className="mr-4">Likes: {currentSong[currentSongIndex].numberOfLikes}</span>
                    <span>Shares: {currentSong[currentSongIndex].reShares}</span>
                </div>
                <AudioPlayer
                    ref={playerRef}
                    autoPlay={isPlaying}
                    src={currentSong[currentSongIndex].audioSrc}
                    onPlay={() => setIsPlaying(true)}
                    onPause={() => setIsPlaying(false)}
                    onClickNext={handleNext}
                    onClickPrevious={handlePrevious}
                    showSkipControls={true}
                    showJumpControls={true}
                    layout="stacked"
                    showDownloadProgress={false}
                    progressJumpSteps={{backward: 5000, forward: 5000}}
                    customProgressBarSection={[
                        RHAP_UI.CURRENT_TIME,
                        RHAP_UI.PROGRESS_BAR,
                        RHAP_UI.DURATION,
                    ]}
                    customControlsSection={[
                        RHAP_UI.ADDITIONAL_CONTROLS,
                        RHAP_UI.MAIN_CONTROLS,
                        RHAP_UI.VOLUME_CONTROLS,
                    ]}
                    customIcons={{
                        play: (
                            <svg className="w-8 h-8 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor" d="M8,5.14V19.14L19,12.14L8,5.14Z"/>
                            </svg>
                        ),
                        pause: (
                            <svg className="w-8 h-8 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor" d="M14,19H18V5H14M6,19H10V5H6V19Z"/>
                            </svg>
                        ),
                        rewind: (
                            <svg className="w-6 h-6 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor" d="m11.5 12l8.5 6V6m-9 12V6l-8.5 6z"/>
                            </svg>
                        ),
                        forward: (
                            <svg className="w-6 h-6 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor" d="M13 6v12l8.5-6M4 18l8.5-6L4 6z"/>
                            </svg>
                        ),
                        loop: (
                            <svg className="w-6 h-6 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor"
                                      d="M17 17H7v-3l-4 4 4 4v-3h12v-6h-2M7 7h10v3l4-4-4-4v3H5v6h2V7z"/>
                            </svg>
                        ),
                        volume: (
                            <svg className="w-6 h-6 text-sky-500 dark:text-sky-400" viewBox="0 0 24 24">
                                <path fill="currentColor"
                                      d="M14 3.23v2.06c2.89.86 5 3.54 5 6.71s-2.11 5.84-5 6.7v2.07c4-.91 7-4.49 7-8.77s-3-7.86-7-8.77M16.5 12c0-1.77-1-3.29-2.5-4.03V16c1.5-.71 2.5-2.24 2.5-4M3 9v6h4l5 5V4L7 9z"/>
                            </svg>
                        ),
                    }}
                />
            </div>
        </div>
    );
};


export default SongPlayer;
