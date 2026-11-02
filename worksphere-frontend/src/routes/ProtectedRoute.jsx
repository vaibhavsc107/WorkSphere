
import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

const ProtectedRoute = ({ allowedRoles, children }) => {
  const { isAuthenticated, isInitializing, user } = useAuth();

  console.log("Authenticated:", isAuthenticated);
  console.log("Initializing:", isInitializing);
  console.log("User:", user);
  console.log("Allowed roles:", allowedRoles);

  if (isInitializing) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <p className="text-lg font-semibold">
          Restoring your session...
        </p>
      </div>
    );
  }

  // Check authentication
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Check authorization
  const hasPermission = user?.roles?.some((role) =>
    allowedRoles.includes(role)
  );

  if (!hasPermission) {
    if (user?.roles?.includes("SUPER_ADMIN")) {
      return <Navigate to="/admin/dashboard" replace />;
    }

    if (user?.roles?.includes("HR_MANAGER")) {
      return <Navigate to="/hr/dashboard" replace />;
    }

    return <Navigate to="/employee/dashboard" replace />;
  }

  return children;
};

export default ProtectedRoute;
