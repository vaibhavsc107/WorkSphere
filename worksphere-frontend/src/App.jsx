
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import DashboardLayout from "./components/layout/DashboardLayout";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ProtectedRoute from "./routes/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
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

        <Route path="*" element={<Navigate to="/login" replace />} />
        
      </Routes>
    </BrowserRouter>
  );
}

export default App;
