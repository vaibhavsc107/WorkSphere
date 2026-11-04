
import { useEffect, useState } from "react";
import { toast } from "sonner";

import {
  User,
  Mail,
  Phone,
  Calendar,
  MapPin,
  Briefcase,
  Building2,
  Pencil,
  LoaderCircle,
  Camera,
} from "lucide-react";

import {
  getMyProfile,
  getMyProfileImage,
  updateMyProfile,
  uploadProfileImage,
  removeProfileImage,
} from "../../services/employeeService";


export default function EmployeeProfile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [profileImage, setProfileImage] = useState(null);

  const [isEditing, setIsEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  const [uploadingImage, setUploadingImage] = useState(false);
  const [imageError, setImageError] = useState("");

  const [previewImage, setPreviewImage] = useState(null);

  const [imageSuccess, setImageSuccess] = useState("");

  const [validationErrors, setValidationErrors] = useState({});

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    dateOfBirth: "",
    gender: "",
    address: "",
  });

  useEffect(() => {
  let imageUrl;

  const loadProfileImage = async () => {
    try {
      const imageBlob = await getMyProfileImage();

      imageUrl = URL.createObjectURL(imageBlob);

      setProfileImage(imageUrl);
    } catch (error) {
      console.log("No profile image available.");
    }
  };

  loadProfileImage();

  return () => {
    if (imageUrl) {
      URL.revokeObjectURL(imageUrl);
    }
  };
}, []);

  

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const data = await getMyProfile();
        setProfile(data);

setFormData({
  firstName: data.firstName || "",
  lastName: data.lastName || "",
  phone: data.phone || "",
  dateOfBirth: data.dateOfBirth || "",
  gender: data.gender || "",
  address: data.address || "",
});
      } catch (err) {
        setError(
          err.response?.data?.message ||
          "Unable to load employee profile."
        );
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, []);

  const validateProfile = () => {
  const errors = {};

  if (!formData.firstName.trim()) {
    errors.firstName = "First name is required.";
  }

  if (!formData.lastName.trim()) {
    errors.lastName = "Last name is required.";
  }

  if (!/^\d{10}$/.test(formData.phone)) {
    errors.phone = "Enter a valid 10-digit phone number.";
  }

  if (!formData.dateOfBirth) {
    errors.dateOfBirth = "Date of birth is required.";
  } else if (new Date(formData.dateOfBirth) >= new Date()) {
    errors.dateOfBirth = "Date of birth must be in the past.";
  }

  if (!formData.gender) {
    errors.gender = "Please select your gender.";
  }

  if (formData.address.length > 500) {
    errors.address = "Address cannot exceed 500 characters.";
  }

  setValidationErrors(errors);

  return Object.keys(errors).length === 0;
};

const handleFieldChange = (e) => {
  const { name, value } = e.target;

  setFormData((previous) => ({
    ...previous,
    [name]: value,
  }));

  setValidationErrors((previous) => ({
    ...previous,
    [name]: "",
  }));
};

  const handleSaveProfile = async (e) => {
  e.preventDefault();

  if (saving) return;

  if (!validateProfile()) {
    return;
  }

  try {
    setSaving(true);
    setError("");

    const updatedProfile = await updateMyProfile(formData);

    setProfile(updatedProfile);
    toast.success("Profile updated successfully!");  
    setIsEditing(false);

  } catch (err) {

    toast.error(
  err.response?.data?.message || "Unable to update profile."
);
    setError(
      err.response?.data?.message ||
      "Unable to update profile."
    );
  } finally {
    setSaving(false);
  }
};

const handleImageSelect = (e) => {
  const file = e.target.files?.[0];

  if (!file) return;

  const allowedTypes = ["image/jpeg", "image/png", "image/webp"];

  if (!allowedTypes.includes(file.type)) {
    setImageError("Only JPG, PNG and WebP images are allowed.");
    e.target.value = "";
    return;
  }

  if (file.size > 2 * 1024 * 1024) {
    setImageError("Image size must not exceed 2 MB.");
    e.target.value = "";
    return;
  }

  setImageError("");
  setPreviewImage(URL.createObjectURL(file));
};

const handleRemoveImage = async () => {
  if (!window.confirm("Are you sure you want to remove your profile picture?")) {
    return;
  }

  try {
    setUploadingImage(true);
    setImageError("");
    setImageSuccess("");

    await removeProfileImage();

    if (profileImage) {
      URL.revokeObjectURL(profileImage);
    }

    if (previewImage) {
      URL.revokeObjectURL(previewImage);
    }

    setProfileImage(null);
    setPreviewImage(null);

    const input = document.getElementById("profileImageInput");
    if (input) input.value = "";

    setProfile((previous) => ({
      ...previous,
      profileImage: null,
    }));

    toast.success("Profile picture removed successfully!");
  } catch (err) {
    toast.error(
  err.response?.data?.message || "Unable to remove profile picture."
);
    setImageError(
      err.response?.data?.message ||
      "Unable to remove profile picture."
    );
  } finally {
    setUploadingImage(false);
  }
};

const handleImageUpload = async () => {
  const input = document.getElementById("profileImageInput");
  const file = input?.files?.[0];

  if (!file) {
    setImageError("Please select an image first.");
    return;
  }

  try {
    setUploadingImage(true);
    setImageError("");
    setImageSuccess("");

    await uploadProfileImage(file);

    // Refresh employee profile data
    const updatedProfile = await getMyProfile();
    setProfile(updatedProfile);
    

    // Reload the saved image from the backend
    const imageBlob = await getMyProfileImage();
    const newImageUrl = URL.createObjectURL(imageBlob);

    setProfileImage((previousImage) => {
      if (previousImage) {
        URL.revokeObjectURL(previousImage);
      }
      return newImageUrl;
    });

    setPreviewImage(null);
    input.value = "";
    toast.success("Profile picture uploaded successfully!");
  } catch (err) {

    toast.error(
  err.response?.data?.message || "Unable to upload profile picture."
);
    setImageError(
      err.response?.data?.message ||
      "Unable to upload profile picture."
    );
  } finally {
    setUploadingImage(false);
  }
};

  if (loading) {
  return (
    <div className="min-h-screen bg-slate-50 p-3 sm:p-5 lg:p-8">
      <div className="mx-auto max-w-6xl animate-pulse space-y-6">

        {/* Page heading */}
        <div className="space-y-2">
          <div className="h-7 w-40 rounded bg-slate-200" />
          <div className="h-4 w-64 rounded bg-slate-200" />
        </div>

        {/* Profile header */}
        <div className="flex flex-col gap-5 rounded-2xl border bg-white p-6 sm:flex-row sm:items-center">
          <div className="h-24 w-24 shrink-0 rounded-full bg-slate-200" />

          <div className="flex-1 space-y-3">
            <div className="h-5 w-48 rounded bg-slate-200" />
            <div className="h-4 w-32 rounded bg-slate-200" />
            <div className="h-6 w-24 rounded-full bg-slate-200" />
          </div>

          <div className="h-10 w-32 rounded-lg bg-slate-200" />
        </div>

        {/* Completion card */}
        <div className="space-y-4 rounded-2xl border bg-white p-6">
          <div className="h-5 w-44 rounded bg-slate-200" />
          <div className="h-3 w-full rounded-full bg-slate-200" />
        </div>

        {/* Information card */}
        <div className="rounded-2xl border bg-white p-6">
          <div className="mb-6 h-5 w-64 rounded bg-slate-200" />

          <div className="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-3">
            {Array.from({ length: 9 }).map((_, index) => (
              <div key={index} className="flex items-center gap-3">
                <div className="h-10 w-10 rounded-lg bg-slate-200" />

                <div className="flex-1 space-y-2">
                  <div className="h-3 w-20 rounded bg-slate-200" />
                  <div className="h-4 w-32 rounded bg-slate-200" />
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}

  if (error) {
    return (
      <div className="rounded-xl border border-red-200 bg-red-50 p-5 text-red-700">
        {error}
      </div>
    );
  }

  if (!profile) return null;

  const profileFields = [
  profile.firstName,
  profile.lastName,
  profile.phone,
  profile.dateOfBirth,
  profile.gender,
  profile.address,
  profile.departmentName,
  profile.designationName,
  profile.joiningDate,
  profile.profileImage,
];

const completedFields = profileFields.filter(
  (field) => field && String(field).trim() !== ""
).length;

const completionPercentage = Math.round(
  (completedFields / profileFields.length) * 100
);

  const details = [
    { label: "First Name", value: profile.firstName, icon: User },
    { label: "Last Name", value: profile.lastName, icon: User },
    { label: "Email Address", value: profile.email, icon: Mail },
    { label: "Phone Number", value: profile.phone, icon: Phone },
    { label: "Date of Birth", value: profile.dateOfBirth, icon: Calendar },
    { label: "Gender", value: profile.gender, icon: User },
    { label: "Department", value: profile.departmentName, icon: Building2 },
    { label: "Designation", value: profile.designationName, icon: Briefcase },
    { label: "Joining Date", value: profile.joiningDate, icon: Calendar },
    { label: "Address", value: profile.address, icon: MapPin },
  ];

  return (
    <div className="min-h-screen w-full overflow-x-hidden bg-slate-50 p-3 sm:p-5 lg:p-8">
      <div className="mx-auto max-w-6xl space-y-6">

        {/* Page heading */}
        <div>
          <h1 className="text-2xl font-bold text-slate-900">
            My Profile
          </h1>
          <p className="mt-1 text-sm text-slate-500">
            View your personal and employment information.
          </p>
        </div>

        {/* Profile header */}
        <div className="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm sm:p-6">
          <div className="flex flex-col gap-5 sm:flex-row sm:items-center">

            <div className="flex h-24 w-24 shrink-0 items-center justify-center overflow-hidden rounded-full bg-blue-100 text-blue-700">

              {previewImage || profileImage ? (
  <img
    src={previewImage || profileImage}
    alt="Employee profile"
    className="h-full w-full object-cover"
  />
) : (
  <User size={42} />
)}

</div>

<input
  type="file"
  id="profileImageInput"
  accept="image/png,image/jpeg,image/webp"
  className="hidden"
  onChange={handleImageSelect}
/>

<div className="flex flex-col items-start gap-2">
  {/* Change Photo label */}
  <label
  htmlFor="profileImageInput"
  className="flex w-fit cursor-pointer items-center gap-2 rounded-lg border border-slate-300 px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
>
  <Camera size={16} />
  Change Photo
  

</label>

  {/* Image error message */}
  {imageError && (
  <p className="text-sm text-red-600">{imageError}</p>
)}

{imageSuccess && (
  <p className="text-sm font-medium text-green-600">
    {imageSuccess}
  </p>
)}


{previewImage && (
  <button
    type="button"
    onClick={handleImageUpload}
    disabled={uploadingImage}
    className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
  >

    {uploadingImage && (
      <LoaderCircle size={16} className="animate-spin" />
    )}

    {uploadingImage ? "Uploading..." : "Upload Photo"}
  </button>
)}

{profile.profileImage && !previewImage && (
  <button
    type="button"
    onClick={handleRemoveImage}
    disabled={uploadingImage}
    className="rounded-lg border border-red-300 px-4 py-2 text-sm font-medium text-red-600 hover:bg-red-50 disabled:opacity-60"
  >
    Remove Photo
  </button>
)}
</div>

{/* Profile Completion */}
<div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
  <div className="mb-3 flex items-center justify-between">
    <div>
      <h3 className="text-lg font-semibold text-slate-900">
        Profile Completion
      </h3>
      <p className="mt-1 text-sm text-slate-500">
        Complete your personal information.
      </p>
    </div>

    <span className="text-xl font-bold text-blue-600">
      {completionPercentage}%
    </span>
  </div>

  <div className="h-3 w-full overflow-hidden rounded-full bg-slate-100">
    <div
      className="h-full rounded-full bg-blue-600 transition-all duration-500"
      style={{ width: `${completionPercentage}%` }}
    />
  </div>

  <p className="mt-2 text-xs text-slate-500">
    {completedFields} of {profileFields.length} fields completed
  </p>
</div>



            <div className="flex-1">
              <h2 className="text-xl font-bold text-slate-900">
                {profile.firstName} {profile.lastName}
              </h2>

              <p className="mt-1 text-sm text-slate-500">
                {profile.designationName || "Employee"}
              </p>

              <div className="mt-3 flex flex-wrap gap-2">
                <span className="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">
                  {profile.employeeCode}
                </span>

                <span className="rounded-full bg-green-50 px-3 py-1 text-xs font-semibold text-green-700">
                  {profile.employmentStatus}
                </span>
              </div>
            </div>

            <button
              type="button"
              onClick={() => setIsEditing(true)}
              className="flex w-full items-center justify-center gap-2 rounded-lg bg-blue-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-blue-700 sm:w-auto"
            >
              <Pencil size={16} />
              Edit Profile
            </button>
          </div>
        </div>

        {/* Personal information */}
<div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
  <div className="mb-6 flex items-center justify-between">
    <h3 className="text-lg font-semibold text-slate-900">
      {isEditing ? "Edit Personal Information" : "Personal & Employment Information"}
    </h3>
  </div>

  {isEditing ? (
    <form className="space-y-5" onSubmit={handleSaveProfile}>
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2">

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">
            First Name
          </label>
          <input
            type="text"
            value={formData.firstName}
            name="firstName"
            onChange={handleFieldChange}
            disabled={saving}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          />
          {validationErrors.firstName && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.firstName}
  </p>
)}
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">
            Last Name
          </label>
          <input   
            type="text"
            value={formData.lastName}     
            name="lastName"
            onChange={handleFieldChange}   
            disabled={saving}    
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          />
          {validationErrors.lastName && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.lastName}
  </p>
)}
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">
            Phone Number
          </label>
          <input
            type="tel"
            value={formData.phone}
            name="phone"
            onChange={handleFieldChange}
            disabled={saving}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          />
          {validationErrors.phone && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.phone}
  </p>
)}
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">
            Date of Birth
          </label>
          <input
            type="date"
            value={formData.dateOfBirth}
            name="dateOfBirth"
            onChange={handleFieldChange}
            disabled={saving}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          />
          {validationErrors.dateOfBirth && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.dateOfBirth}
  </p>
)}
        </div>

        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700">
            Gender
          </label>
          <select
            value={formData.gender}
            name="gender"
            onChange={handleFieldChange}
            disabled={saving}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          >
            <option value="">Select Gender</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
            <option value="PREFER_NOT_TO_SAY">Prefer not to say</option>
          </select>
          {validationErrors.gender && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.gender}
  </p>
)}
        </div>

        <div className="sm:col-span-2">
          <label className="mb-1 block text-sm font-medium text-slate-700">
            Address
          </label>
          <textarea
            rows="3"
            value={formData.address}
            name="address"
            onChange={handleFieldChange}
            disabled={saving}
            className="w-full rounded-lg border border-slate-300 px-3 py-2 outline-none focus:border-blue-500"
          />
          {validationErrors.address && (
  <p className="mt-1 text-xs text-red-600">
    {validationErrors.address}
  </p>
)}
        </div>
      </div>

      <div className="flex justify-end gap-3">
        <button
          type="button"
          onClick={() => setIsEditing(false)}
          className="rounded-lg border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
        >
          Cancel
        </button>

        <button
  type="submit"
  disabled={saving}
  className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
>
  {saving ? "Saving..." : "Save Changes"}
</button>
      </div>
    </form>
  ) : (
    <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
      {details.map(({ label, value, icon: Icon }) => (
        <div key={label} className="flex gap-3">
          <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-lg bg-slate-100 text-slate-600">
            <Icon size={19} />
          </div>

          <div className="min-w-0">
            <p className="text-xs text-slate-500">{label}</p>
            <p className="mt-1 break-words text-sm font-medium text-slate-800">
              {value || "Not provided"}
            </p>
          </div>
        </div>
      ))}
    </div>
  )}
</div>

      </div>
    </div>
  );
}
