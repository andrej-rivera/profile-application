import { useState } from 'react'
import { useNavigate } from 'react-router'

import './App.css'

function App() {
  const navigate = useNavigate();

  return (
    <>
      <section id="center">
        <div>
          <h1>Profile Application</h1>
          <p>
            Welcome, please <code>login</code> or <code>sign up</code>
          </p>
        </div>
        <button onClick={() => navigate("/login")}>Login</button>
        <button onClick={() => navigate("/signup")}>Sign Up</button>
      </section>

    </>
  )
}

export default App
