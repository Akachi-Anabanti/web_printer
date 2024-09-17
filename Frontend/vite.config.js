// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react-swc'
// # (so you can import "path" without error)


// https://vitejs.dev/config/
import path from "path"
import react from "@vitejs/plugin-react-swc"
import { defineConfig } from "vite"

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      '/api':{
        target: 'http://localhost:3001',
        changeOrigin: true,
        secure: false
      }
    }
  }
})

