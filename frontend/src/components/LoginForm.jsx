import {useState} from "react";

export default function LoginForm(props) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");


    function handleSubmit(e) {
        e.preventDefault();
        const user = {username, password};

    }

    return (
        <form onSubmit={}>
            <label htmlFor="username">Username: </label>
            <input type="text" id="username" name="username" placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
            <label htmlFor="password">Password: </label>
            <input type="password" id="password" name="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
            <button type="submit">Login</button>
        </form>
    )
}
