
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },

  // Allow browser to send and receive HttpOnly cookies
  withCredentials: true,
});

export default api;
