import {useState} from "react";
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";
import {API_BASE_URL} from "../../config.js";

export default function SignupForm() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();

        const user = {name: username, email, password};
        try{
            if(password !== confirmPassword){
                setError("Passwords don't match");
            }else{
                const response = await axios.post(`${API_BASE_URL}/api/auth/signup`, user);
                localStorage.setItem('token', response.data.token);
                if (response.status === 201) {
                    navigate('/login');
                }
            }
        } catch (error) {
            setError(`Error: ${error}`);
        }
    }

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-900 text-white">
            <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-80">
                <h2 className="text-2xl font-semibold text-center mb-4">Register</h2>
                {error && <p className="text-red-500 text-sm text-center mb-4">{error}</p>}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <input
                            id="username"
                            type="text"
                            className="w-full px-4 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                            name="username"
                            placeholder="Username"
                            onChange={e => setUsername(e.target.value)}
                        />
                    </div>
                    <input
                        id="email"
                        type="email"
                        className="w-full px-4 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Email address"
                        name="email"
                        onChange={e => setEmail(e.target.value)}
                    />
                    <input
                        id="password"
                        type="password"
                        className="w-full px-4 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Password"
                        name="password"
                        onChange={e => setPassword(e.target.value)}
                    />
                    <input
                        id="password"
                        type="password"
                        className="w-full px-4 py-2 bg-gray-700 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Confirm Password"
                        name="password"
                        onChange={e => setConfirmPassword(e.target.value)}
                    />
                    <button
                        type="submit"
                        className="w-full p-2 bg-blue-600 hover:bg-blue-700 rounded text-white font-bold"
                    >
                        Submit
                    </button>
                </form>
                <div className="flex justify-center mt-4">
                    <Link to={'/login'} className="text-blue-400 hover:text-blue-600">
                        <p className="text-center">Already registered? Click here to log in!</p>
                    </Link>
                </div>
            </div>
        </div>
    );
}
