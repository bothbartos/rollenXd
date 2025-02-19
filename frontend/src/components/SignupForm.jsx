import {useState} from "react";
import axios from "axios";

export default function SignupForm() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();
        const user = {name: username, email, password};
        try{
            const response = await axios.post('/api/auth/signup', user);
            localStorage.setItem('token', response.data.token);

        } catch (error) {
            setError(`Error: ${error}`);
        }
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-900">
            <form onSubmit={handleSubmit} className="bg-gray-800 p-6 rounded-lg shadow-lg w-96 text-white">
                {error && <p className="text-red-500 text-sm text-center mb-4">{error}</p>}
                <label htmlFor="username" className="block text-sm font-medium text-gray-300">Username</label>
                <input
                    id="username"
                    type="text"
                    className="w-full p-2 mb-4 bg-gray-700 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    name="username"
                    onChange={e => setUsername(e.target.value)}
                />
                <label htmlFor="email" className="block text-sm font-medium text-gray-300">Email address:</label>
                <input
                    id="email"
                    type="email"
                    className="w-full p-2 mb-4 bg-gray-700 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    name="email"
                    onChange={e => setEmail(e.target.value)}
                />
                <label htmlFor="password" className="block text-sm font-medium text-gray-300">Password</label>
                <input
                    id="password"
                    type="password"
                    className="w-full p-2 mb-4 bg-gray-700 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    name="password"
                    onChange={e => setPassword(e.target.value)}
                />
                <button
                    type="submit"
                    className="w-full p-2 bg-blue-600 hover:bg-blue-700 rounded text-white font-bold"
                >
                    Submit
                </button>
            </form>
        </div>
    );
}
