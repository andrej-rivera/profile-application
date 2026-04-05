import { useState } from 'react'
import './App.css'
import { useNavigate } from 'react-router'

const API_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080"

function Login() {
    const navigate = useNavigate(); // initialize navigate function
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const loginButton = async (event) => {

        // handle submission event
        event.preventDefault() // prevent page refresh for forms

        // format post data
        const postData = {
            username: username,
            password: password
        }

        // make fetch request
        try {
            const response = await fetch(`${API_URL}/login`, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(postData)
            });



            const result = await response.text();

            if (!response.ok) {
                throw new Error("Login Failed: " + response.status + " " + result);
            }
            
            console.log(result);
            localStorage.setItem('logged_user', true);

            navigate("/dashboard"); // redirect to dashboard on login

        } catch (error) {
            console.error(error);
        }
    }
  


    return (
        <>
            <section id="header">
                <h1>Login</h1>
            </section>
            <section id="center">
                <form className="login-form" method="POST" onSubmit={loginButton}>
                    <div>
                        <label htmlFor="username">Username:</label>
                        <input type="text" id="username" name="username"
                        value={username} onChange={(e) => setUsername(e.target.value)} required/>
                    </div>
                    
                    <div>
                        <label htmlFor="password">Password:</label>
                        <input type="password" id="password" name="password"
                        value={password} onChange={(e) => setPassword(e.target.value)} required/>
                    </div>
                    
                    <button type="submit">Login</button>
                </form>
            </section>
        </>
    )
}

export default Login
