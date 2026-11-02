
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Eye, EyeOff, LockKeyhole, Mail } from "lucide-react";
import useAuth from "../hooks/useAuth";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await login({ email, password });

      const roles = response.roles || [];

      if (roles.includes("SUPER_ADMIN")) {
        navigate("/admin/dashboard");
      } else if (roles.includes("HR_MANAGER")) {
        navigate("/hr/dashboard");
      } else {
        navigate("/employee/dashboard");
      }
    } catch (err) {
      setError(
        err.response?.data?.message ||
        "Invalid email or password. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen grid lg:grid-cols-2 bg-slate-50">

      {/* Branding Section */}
      <div className="hidden lg:flex flex-col justify-between bg-slate-950 p-12 text-white">
        <h1 className="text-3xl font-bold">
          Work<span className="text-blue-400">Sphere</span>
        </h1>

        <div>
          <h2 className="text-5xl font-bold leading-tight">
            Manage your workforce.
            <span className="text-blue-400">
              {" "}Empower your people.
            </span>
          </h2>

          <p className="mt-6 text-slate-300 text-lg max-w-lg">
            One unified platform for employee management,
            attendance, leave, payroll and performance.
          </p>
        </div>

        <p className="text-sm text-slate-400">
          © 2026 WorkSphere. All rights reserved.
        </p>
      </div>

      {/* Login Form */}
      <div className="flex items-center justify-center p-6 sm:p-12">
        <div className="w-full max-w-md">

          <div className="mb-10">
            <h2 className="text-3xl font-bold text-slate-900">
              Welcome back
            </h2>

            <p className="mt-2 text-slate-500">
              Sign in to your WorkSphere account.
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">

            {/* Email */}
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Email address
              </label>

              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={20} />

                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="you@company.com"
                  required
                  autoComplete="email"
                  className="w-full rounded-xl border border-slate-300 py-3 pl-11 pr-4 outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                />
              </div>
            </div>

            {/* Password */}
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Password
              </label>

              <div className="relative">
                <LockKeyhole className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={20} />

                <input
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter your password"
                  required
                  autoComplete="current-password"
                  className="w-full rounded-xl border border-slate-300 py-3 pl-11 pr-12 outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                />

                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400"
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                </button>
              </div>
            </div>

            {/* Error */}
            {error && (
              <div className="rounded-lg bg-red-50 p-3 text-sm text-red-600">
                {error}
              </div>
            )}

            {/* Submit */}
            <button
              type="submit"
              disabled={loading}
              className="w-full rounded-xl bg-blue-600 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
            >
              {loading ? "Signing in..." : "Sign in"}
            </button>

          </form>

          <p className="mt-8 text-center text-sm text-slate-500">
            Don't have an account?{" "}
            <Link to="/register" className="font-semibold text-blue-600 hover:text-blue-700">
              Create account
            </Link>
          </p>

        </div>
      </div>
    </div>
  );
};

export default Login;
