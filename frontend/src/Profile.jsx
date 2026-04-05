import { useState, useEffect, useContext } from 'react'
import { useNavigate, useParams } from 'react-router'

import './App.css'

function Profile() {
    const [id, setId] = useState('')
    const [username, setUsername] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [bio, setBio] = useState('')
    const [profilePicture, setProfilePicture] = useState(null)

    let params = useParams(); // params.username will contain the username from the URL path
    const navigate = useNavigate();

    const fetchProfile = async () => {
        // fetch user profile data from backend
        try {
            const response = await fetch(`http://localhost:8080/user/profile/${params.username}`, {
                method: "GET",
                cache: "no-store",
                credentials: "include"
            });


            const profileData = await response.json();
            
            
            if (!response.ok) {
                throw new Error("Failed to fetch profile: " + response.status);
            }

            setUsername(params.username);
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

    useEffect(() => {
        console.log("Fetching profile for username:", params.username);
        fetchProfile();
    }, [])

  return (
    <>

        <section id="center">
            <h1>Welcome to {username}'s profile!</h1>
            <h2>User ID: {id}</h2>
            <p>{bio}</p>
            <img src={profilePicture} alt="Profile Picture" />
            <button onClick={() => navigate("/search")}>Return</button>
        </section>
    </>
  )
} 

export default Profile
