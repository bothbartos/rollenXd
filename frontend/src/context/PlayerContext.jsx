import {createContext, useState} from "react";

// eslint-disable-next-line react-refresh/only-export-components
export const PlayerContext = createContext();

export const PlayerProvider = ({ children }) => {
    const [currentSong, setCurrentSong] = useState(null)
    const [history, setHistory] = useState([])

    return (
        <PlayerContext.Provider value={{currentSong, setCurrentSong, history, setHistory}}>
            {children}
        </PlayerContext.Provider>
    )
}

