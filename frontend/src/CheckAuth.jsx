import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router'

import './App.css'

// Redirects to home page if user is not authenticated, otherwise renders the child component
function CheckAuth(props) {
  const navigate = useNavigate(); // initialize navigate function
  const redirect = async () => {
    const response = await fetch("http://localhost:8080/user/profile", {
      method: "GET",
      credentials: "include",
      headers: {
        "Content-Type": "application/json"
      }
    })

    if (response.status === 400 || response.status === 401 || response.status === 403) {
      localStorage.clear();
      navigate("/");
    }
  }
  useEffect(() => {
    redirect();
  }, [])


  return (
    <>
      {props.children}
    </>
  )
}

export default CheckAuth
