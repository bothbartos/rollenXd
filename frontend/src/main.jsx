import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import './index.css'
import App from './App.jsx'
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const queryClient = new QueryClient();

const router = createBrowserRouter([{
    path: '/',
    children: [
        {
            path: '/',
            element: <App />,
        }
    ]
    }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
      </QueryClientProvider>
  </StrictMode>,
)
