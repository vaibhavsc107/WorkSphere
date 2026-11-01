
import { useEffect, useState } from "react";
import { testConnection } from "./services/testService";

function App() {

    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const fetchBackendData = async () => {

            try {
                const result = await testConnection();

                setData(result);
            }
            catch (err) {
                setError(
                    err.response?.data?.message ||
                    err.message ||
                    "Unable to connect to backend"
                );
            }
            finally {
                setLoading(false);
            }
        };

        fetchBackendData();

    }, []);

    if (loading) {
        return <h2>Connecting to WorkSphere backend...</h2>;
    }

    if (error) {
        return <h2>Connection failed: {error}</h2>;
    }

    return (
        <div style={{ padding: "30px" }}>

            <h1>WorkSphere</h1>

            <h2>Backend Connection Status</h2>

            <p>{data.message}</p>

            <p>Application: {data.application}</p>

            <p>Status: {data.status}</p>

        </div>
    );
}

export default App;
