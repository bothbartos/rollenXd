import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import NavBar from "./components/NavBar.jsx";
import ResultPage from "./pages/ResultPage.jsx";
import UploadSongForm from "./components/UploadSongForm.jsx";
import PlaySongPage from "./pages/PlaySongPage.jsx";
import {PlayerProvider} from "./context/PlayerContext.jsx";
import SongDetailPage from "./components/SongDetailPage.jsx";
import LoginForm from "./components/LoginForm.jsx";
import SignupForm from "./components/SignupForm.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

const queryClient = new QueryClient();

const router = createBrowserRouter([
    {
        path: "/",
        element: <NavBar />,
        children: [
            { path: "/login", element: <LoginForm /> },
            { path: "/signup", element: <SignupForm /> },

            {
                element: <ProtectedRoute />,
                children: [
                    { path: "/", element: <App /> },
                    { path: "/search", element: <ResultPage /> },
                    { path: "/upload-song", element: <UploadSongForm /> },
                    { path: "/play-song", element: <PlaySongPage /> },
                    { path: "/songDetails/:id", element: <SongDetailPage /> }
                ]
            }
        ]
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <QueryClientProvider client={queryClient}>
          <PlayerProvider>
              <RouterProvider router={router} />
          </PlayerProvider>
      </QueryClientProvider>
  </StrictMode>,
)
