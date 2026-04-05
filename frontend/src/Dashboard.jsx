import { useState, useEffect, useContext } from 'react'
import { useNavigate } from 'react-router'


import './App.css'

function Dashboard() {
    const [id, setId] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [bio, setBio] = useState('')
    const [profilePicture, setProfilePicture] = useState(null)
    
    const navigate = useNavigate();

    const fetchProfile = async () => {
        // fetch user profile data from backend
        try {
            const response = await fetch("http://localhost:8080/user/profile", {
                method: "GET",
                credentials: "include"
            });


            const profileData = await response.json();
            
            
            if (!response.ok) {
                throw new Error("Failed to fetch profile: " + response.status);
            }


            setId(profileData.id);
            setFirstName(profileData.firstName);
            setLastName(profileData.lastName);
            setBio(profileData.bio);
            setProfilePicture(profileData.profilePictureUrl);
            console.log("Profile data fetched successfully:", profileData);
        } catch (error) {
            console.error(error);
        }
    };

    // method for logging out
    const logout = async () => {
        try {
            const response = await fetch("http://localhost:8080/logout", {
                method: "POST",
                credentials: "include"
            });

            const result = await response.text;
            if (!response.ok) {
                throw new Error("Logout error: " + response.status);
            }

            // on successful logout, redirect to home & clear log-in state
            localStorage.clear('logged_user');
            navigate("/");

        } catch(error) {
            console.error(error);
        }
    };

    const setProfile = () => {
        const currentProfileData = {
            firstName: firstName,
            lastName: lastName,
            bio: bio,
            profilePictureUrl: profilePicture
        };
        navigate("/set-profile", {state: 
            currentProfileData
        });
    }

    const search = () => {
        navigate("/search");
    }


    useEffect(() => {

        fetchProfile();
    }, [])

  return (
    <>
        <section id="center">
            <h1>Welcome, {firstName} {lastName}!</h1>
            <h2>User ID: {id}</h2>
            <p>{bio}</p>
            <img src={profilePicture} alt="Profile Picture" />
            <div>
                <button onClick={logout}>Log-out</button>
                <button onClick={search}>Search</button>
                <button onClick={setProfile}>Set Profile</button>
            </div>
        </section>
    </>
  )
} 

export default Dashboard
