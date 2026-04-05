import { useState, useEffect } from 'react'
import { useNavigate, useLocation } from 'react-router'

import './App.css'

const API_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080"

function SetProfile() {
    const navigate = useNavigate(); // initialize navigate function
    const location = useLocation();
    const profileData = location.state || {
        firstName : "",
        lastName: "",
        bio: "",
        profilePictureUrl: ""
    };

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [bio, setBio] = useState("");
    const [pictureUrl, setPictureUrl] = useState("");

    const saveProfile = async (event) => {
        event.preventDefault()
        const postData = {
            id: 0,
            firstName: firstName,
            lastName: lastName,
            bio: bio,
            profilePictureUrl: pictureUrl
        }
        try {
            const response = await fetch(`${API_URL}/user/set-profile`, {
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
            
            console.log("Set Profile Successfully :D");

            navigate("/dashboard"); // redirect to dashboard on login

        } catch (error) {
            console.error(error);
        }
    };


    useEffect(() => {
        setFirstName(profileData.firstName);
        setLastName(profileData.lastName);
        setBio(profileData.bio);
        setPictureUrl(profileData.profilePictureUrl);
    }, []) // pass in empty brackets to run only once (after first render)


    return (
        <>
            <section id="header">
                <h1>Set Profile</h1>
            </section>
            <section id="center">
                <form className="profile-form" method="POST" onSubmit={saveProfile}>
                    <div>
                        <label htmlFor="firstname">First Name:</label>
                        <input type="text" id="firstname" name="firstname"
                        value={firstName} onChange={(e) => setFirstName(e.target.value)} />
                    </div>
                    
                    <div>
                        <label htmlFor="lastname">Last Name:</label>
                        <input type="text" id="lastname" name="lastname"
                        value={lastName} onChange={(e) => setLastName(e.target.value)}/>
                    </div>

                    <div>
                        <label htmlFor="bio">Bio:</label>
                        <input type="text" id="bio" name="bio"
                        value={bio} onChange={(e) => setBio(e.target.value)} />
                    </div>

                    <div>
                        <label htmlFor="pictureUrl">Profile Picture URL:</label>
                        <input type="text" id="pictureUrl" name="pictureUrl"
                        value={pictureUrl} onChange={(e) => setPictureUrl(e.target.value)}/>
                    </div>
                    
                    <button type="submit">Save</button>
                </form>
            </section>
        </>
    )
}

export default SetProfile