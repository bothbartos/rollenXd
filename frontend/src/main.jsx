import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import NavBar from "./components/NavBar.jsx";
import ResultPage from "./pages/ResultPage.jsx";
import UploadSongForm from "./pages/UploadSongForm.jsx";
import {PlayerProvider} from "./context/PlayerContext.jsx";
import SongDetailPage from "./pages/SongDetailPage.jsx";
import LoginForm from "./pages/LoginForm.jsx";
import SignupForm from "./pages/SignupForm.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

const queryClient = new QueryClient();

const router = createBrowserRouter([
    {
        path: "/",
        element: <NavBar />,
        children: [
            { path: "/login", element: <LoginForm /> },
            { path: "/signup", element: <SignupForm /> },
            { path: "/", element: <App /> },
            { path: "/search", element: <ResultPage /> },
            { path: "/songDetails/:id", element: <SongDetailPage /> },
            {
                element: <ProtectedRoute />,
                children: [
                    { path: "/upload-song", element: <UploadSongForm /> },
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
