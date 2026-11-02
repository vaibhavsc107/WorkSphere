import axios from "axios";
import api from "./api";

const setupInterceptors = (
  getAccessToken,
  setAccessToken,
  onSessionExpired
) => {
  // Attach access token to outgoing API requests
  const requestInterceptor = api.interceptors.request.use(
    (config) => {
      const token = getAccessToken();

      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      return config;
    },
    (error) => Promise.reject(error)
  );

  // Handle expired access tokens
  const responseInterceptor = api.interceptors.response.use(
    (response) => response,

    async (error) => {
      const originalRequest = error.config;

      if (
        error.response?.status === 401 &&
        originalRequest &&
        !originalRequest._retry &&
        !originalRequest.url?.includes("/auth/login") &&
        !originalRequest.url?.includes("/auth/refresh") &&
        !originalRequest.url?.includes("/auth/logout")
      ) {
        originalRequest._retry = true;

        try {
          const response = await axios.post(
            `${api.defaults.baseURL}/auth/refresh`,
            {},
            {
              withCredentials: true,
            }
          );

          const newAccessToken = response.data.accessToken;

          setAccessToken(newAccessToken);

          originalRequest.headers.Authorization =
            `Bearer ${newAccessToken}`;

          // Retry the original request
          return api(originalRequest);

        } catch (refreshError) {
          onSessionExpired();
          return Promise.reject(refreshError);
        }
      }

      return Promise.reject(error);
    }
  );

  // Return cleanup function
  return () => {
    api.interceptors.request.eject(requestInterceptor);
    api.interceptors.response.eject(responseInterceptor);
  };
};

export default setupInterceptors;