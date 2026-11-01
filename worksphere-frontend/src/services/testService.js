
import api from "./api";

export const testConnection = async () => {
    const response = await api.get("/test");

    return response.data;
};
