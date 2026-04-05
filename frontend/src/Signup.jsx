import { useState } from 'react'
import './App.css'
import { useNavigate } from 'react-router'

function Login() {
    const navigate = useNavigate(); // initialize navigate function
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [email, setEmail] = useState('')
    const signupButton = async (event) => {

        // handle submission event
        event.preventDefault() // prevent page refresh

        // format post data
        const postData = {
            username: username,
            password: password,
            email: email,
            roles: "USER"
        }

        // make fetch request
        try {
            const response = await fetch("http://localhost:8080/register", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(postData)
            });



            const result = await response.text();

            if (!response.ok) {
                throw new Error("Sign Up Failed: " + response.status + " " + result);
            }
            
            console.log(result);

            navigate("/Login"); // redirect to dashboard on login

        } catch (error) {
            console.error(error);
        }
    }
  


    return (
        <>
            <section id="header">
                <h1>Sign Up</h1>
            </section>
            <section id="center">
                <form className="signup-form" method="POST" onSubmit={signupButton}>

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

                    <div>
                        <label htmlFor="password">Email:</label>
                        <input type="email" id="email" name="email"
                        value={email} onChange={(e) => setEmail(e.target.value)} required/>
                    </div>
                    
                    <div><button type="submit">Sign Up</button></div>
                </form>
            </section>
        </>
    )
}

export default Login
