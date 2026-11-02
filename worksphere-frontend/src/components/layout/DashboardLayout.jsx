
import { NavLink, useNavigate } from "react-router-dom";
import { LayoutDashboard, LogOut, Users, Building2 } from "lucide-react";
import useAuth from "../../hooks/useAuth";

const DashboardLayout = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const roles = user?.roles || [];

  const dashboardPath = roles.includes("SUPER_ADMIN")
    ? "/admin/dashboard"
    : roles.includes("HR_MANAGER")
      ? "/hr/dashboard"
      : "/employee/dashboard";

  const navigation = [
    {
      name: "Dashboard",
      path: dashboardPath,
      icon: LayoutDashboard,
    },
    
  ...(user?.roles?.includes("SUPER_ADMIN")
    ? [
        {
            name: "Admin Dashboard",
          label: "Admin Dashboard",
          path: "/admin/dashboard",
          icon: LayoutDashboard,
        },
      ]
    : []),

  ...(user?.roles?.some((role) =>
    ["SUPER_ADMIN", "HR_MANAGER"].includes(role)
  )
    ? [
        {
            name: "HR Dashboard",
          label: "HR Dashboard",
          path: "/hr/dashboard",
          icon: Users,
        },
      ]
    : []),

  {
    name: "Employee Dashboard",
    label: "Employee Dashboard",
    path: "/employee/dashboard",
    icon: LayoutDashboard,
  },

  ...(user?.roles?.some((role) =>
    ["SUPER_ADMIN", "HR_MANAGER"].includes(role)
  )
    ? [
        {
            name: "Employees",
          label: "Employees",
          path: "/employees",
          icon: Users,
        },
        {
          name: "Departments",
          label: "Departments",
          path: "/departments",
          icon: Building2,
        },
      ]
    : []),
];
  

  const handleLogout = async () => {
    await logout();
    navigate("/login", { replace: true });
  };

  return (
    <div className="min-h-screen bg-slate-100 flex">

      {/* Sidebar */}
      <aside className="w-64 bg-slate-950 text-white flex flex-col">

        <div className="p-6 border-b border-slate-800">
          <h1 className="text-2xl font-bold">
            Work<span className="text-blue-400">Sphere</span>
          </h1>
        </div>

        <nav className="flex-1 p-4 space-y-2">
          {navigation.map((item) => {
            const Icon = item.icon;

            return (
              <NavLink
                key={item.name}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-lg px-4 py-3 transition ${
                    isActive
                      ? "bg-blue-600 text-white"
                      : "text-slate-300 hover:bg-slate-800"
                  }`
                }
              >
                <Icon size={20} />
                {item.name}
              </NavLink>
            );
          })}
        </nav>

        <div className="border-t border-slate-800 p-4">
          <div className="mb-4">
            <p className="text-sm text-slate-400">Signed in as</p>
            <p className="truncate font-medium">{user?.email}</p>
            <p className="mt-1 text-xs text-blue-400">
              {roles.join(", ")}
            </p>
          </div>

          <button
            onClick={handleLogout}
            className="flex w-full items-center gap-3 rounded-lg px-4 py-3 text-slate-300 transition hover:bg-red-500/10 hover:text-red-400"
          >
            <LogOut size={20} />
            Logout
          </button>
        </div>

      </aside>

      {/* Main content */}
      <main className="min-w-0 flex-1 p-8">
        {children}
      </main>

    </div>
  );
};

export default DashboardLayout;
