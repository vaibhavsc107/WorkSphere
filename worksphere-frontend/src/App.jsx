
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import { Toaster } from "sonner";
import { toast } from "sonner";


import DashboardLayout from "./components/layout/DashboardLayout";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProtectedRoute from "./routes/ProtectedRoute";
import EmployeeProfile from "./pages/employee/EmployeeProfile";

function App() {
  return (

    
    <BrowserRouter>

    <Toaster
        position="top-right"
        richColors
        closeButton
        duration={3000}
      />

      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="/login" element={<Login />} />

        <Route path="/register" element={<Register />} />

        <Route
            path="/admin/dashboard"
            element={
                <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
                    <DashboardLayout>
                    <h1 className="text-3xl font-bold">Admin Dashboard</h1>
                    </DashboardLayout>
                </ProtectedRoute>
            }
        />


        <Route
          path="/hr/dashboard"
          element={
            <ProtectedRoute allowedRoles={["SUPER_ADMIN", "HR_MANAGER"]}>
                <DashboardLayout>
                    <h1 className="text-3xl font-bold">HR Dashboard</h1>
                </DashboardLayout>
            </ProtectedRoute>
          }
        />

        <Route
          path="/employee/dashboard"
          element={
            <ProtectedRoute allowedRoles={[
                "SUPER_ADMIN",
                "HR_MANAGER",
                "EMPLOYEE",
              ]}
            >
                <DashboardLayout>
                    <h1 className="text-3xl font-bold">Employee Dashboard</h1>
                </DashboardLayout>
            </ProtectedRoute>
          }
        />

          <Route
            path="/employee/profile"
              element={
              <ProtectedRoute allowedRoles={["EMPLOYEE"]}>
                <EmployeeProfile />
              </ProtectedRoute>
              }
            />

        <Route path="*" element={<Navigate to="/login" replace />} />
        
      </Routes>

      

<button
  onClick={() => toast.success("Welcome to WorkSphere!")}
>
  Test Notification
</button>
    </BrowserRouter>
  );
}

export default App;
