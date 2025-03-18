import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react-swc'
import tailwindcss from "@tailwindcss/vite";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(),
    tailwindcss()],
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", // URL of your Spring Boot server
        changeOrigin: true,
      },
    },
  }
})
