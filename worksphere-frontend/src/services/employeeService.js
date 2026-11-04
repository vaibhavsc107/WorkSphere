
import api from "./api";

// Retrieve logged-in employee profile
export const getMyProfile = async () => {
  const response = await api.get("/employees/me");
  return response.data;
};

// Update logged-in employee profile
export const updateMyProfile = async (profileData) => {
  const response = await api.put("/employees/me", profileData);
  return response.data;
};

// Upload profile image
export const uploadProfileImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await api.post(
    "/employees/me/profile-image",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return response.data;
};

// Retrieve profile image
export const getProfileImageUrl = () => {
  return `${import.meta.env.VITE_API_BASE_URL}/employees/me/profile-image`;
};


// Retrieve employee profile image using JWT authentication
export const getMyProfileImage = async () => {
  const response = await api.get("/employees/me/profile-image", {
    responseType: "blob",
  });

  return response.data;
};

export const removeProfileImage = async () => {
  const response = await api.delete("/employees/me/profile-image");
  return response.data;
};