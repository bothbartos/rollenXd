import React, {useContext, useEffect, useState, useRef} from 'react';
import AudioPlayer, {RHAP_UI} from 'react-h5-audio-player';
import {PlayerContext} from "../context/PlayerContext.jsx";

const SongPlayer = () => {
    const {currentSong, setCurrentSong, history} = useContext(PlayerContext);
    const [isPlaying, setIsPlaying] = useState(false);
    const playerRef = useRef(null);

    useEffect(() => {
        const audio = playerRef.current?.audio?.current;

        if (currentSong && audio) {
            audio.play().then(() => setIsPlaying(true)).catch(() => {
            });
        }


    }, [currentSong]);

    const handleNext = () => {
        if (history.length - 1 === history.indexOf(currentSong)) {
            setCurrentSong(history[0])
        } else {
            setCurrentSong(history[history.indexOf(currentSong) + 1]);
        }
    }

    const handlePrevious = () => {
        if (history.indexOf(currentSong) === 0) {
            setCurrentSong(history[0]);
        } else {
            setCurrentSong(history[history.indexOf(currentSong) - 1]);
        }
    }
    if (!currentSong) {
        return null;
    }


    return (
        <div className="fixed bottom-0 left-0 w-full bg-gray-800 z-50">
            <AudioPlayer
                ref={playerRef}
                autoPlay={isPlaying}
                src={currentSong.audioSrc}
                onPlay={() => setIsPlaying(true)}
                onPause={() => setIsPlaying(false)}
                onClickNext={handleNext}
                onClickPrevious={handlePrevious}
                showSkipControls={true}
                showJumpControls={true}
                onEnded={handleNext}
                layout="horizontal-reverse"
                showDownloadProgress={false}
                progressJumpSteps={{backward: 5000, forward: 5000}}
                customProgressBarSection={[
                    RHAP_UI.CURRENT_TIME,
                    RHAP_UI.PROGRESS_BAR,
                    RHAP_UI.DURATION,
                    <div className="flex items-center ml-4 space-x-4">
                        <img
                            className="w-14 h-14 object-cover rounded-md"
                            alt={"Song Cover"}
                            src={`${currentSong.coverSrc}`}
                        />
                        <div className="flex flex-col text-left">
                            <span className="text-white text-sm font-semibold truncate">
                                {currentSong.title}
                            </span>
                            <span className="text-xs text-gray-400 truncate">
                                 {currentSong.author}
                            </span>
                        </div>
                    </div>
                ]}
                customControlsSection={[
                    RHAP_UI.MAIN_CONTROLS,
                    RHAP_UI.ADDITIONAL_CONTROLS,
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
    );
};


export default SongPlayer;
