
import api from "../services/api";
import AuthContext from "./authContext";
import { loginUser } from "../services/authService";

import { useEffect, useRef, useState } from "react";
import setupInterceptors from "../services/setupInterceptors";



const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [accessToken, setAccessToken] = useState(null);
  const [isInitializing, setIsInitializing] = useState(true);

  // Always provides the latest token to Axios
  const accessTokenRef = useRef(accessToken);

  useEffect(() => {
    accessTokenRef.current = accessToken;
  }, [accessToken]);

  useEffect(() => {
    const cleanup = setupInterceptors(
      () => accessTokenRef.current,
      setAccessToken,
      () => {
        setUser(null);
        setAccessToken(null);
      }
    );

    return cleanup;
  }, []);

  useEffect(() => {
  const restoreSession = async () => {
    try {
      const response = await api.post("/auth/refresh");

      setAccessToken(response.data.accessToken);

      setUser({
        email: response.data.email,
        roles: response.data.roles,
      });
    } catch (error) {
      setUser(null);
      setAccessToken(null);
    } finally {
      setIsInitializing(false);
    }
  };

  restoreSession();
}, []);

  const login = async (credentials) => {
    const response = await loginUser(credentials);

    setAccessToken(response.accessToken);

    setUser({
      email: response.email,
      roles: response.roles,
    });

    return response;
  };

  const logout = async () => {
  try {
    await api.post("/auth/logout");
  } catch (error) {
    console.error("Logout request failed:", error);
  } finally {
    setUser(null);
    setAccessToken(null);
  }
};

  const isAuthenticated = Boolean(accessToken);

  return (
    <AuthContext.Provider
      value={{
        user,
        accessToken,
        isAuthenticated,
        isInitializing,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
