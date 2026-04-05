import { useState, useEffect } from 'react'
import './App.css'
import { useNavigate, NavLink } from 'react-router'

const API_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080"

function Search() {
    const navigate = useNavigate(); // initialize navigate function
    const [searchUsername, setSearchUsername] = useState('');
    const [searchCount, setSearchCount] = useState(10);
    const [searchPage, setSearchPage] = useState(0);
    const [searchResults, setSearchResults] = useState([]);
    const search = async () => {
        
        // handle submission event

        // make GET request for usernames
        try {
            const response = await fetch(`${API_URL}/user/search` 
                 + "?username=" + searchUsername
                 + "&i=" + searchCount
                 + "&p=" + searchPage, {
                method: "GET",
                credentials: "include"
            });



            const result = await response.json();

            if (!response.ok) {
                throw new Error("Search Failed: " + response.status + " " + result);
            }
            
            setSearchResults(result);
            console.log(searchResults);
        } catch (error) {
            console.error(error);
        }
    }

    const formatResults = () => {     
        return (
            <ul className="search-content">
                {searchResults.map((username, index) => (
                    <NavLink to={`/profile/${username}`} className="search-result">
                        <li key={index}>{username}</li>
                    </NavLink>
                ))}
            </ul>
    );
    };

    useEffect(() => {
        search();
    }, []);
  


    return (
        <>
            <section>
                <button onClick={() => navigate("/dashboard")}>Return</button>
            </section>
            <section id="header">
                <h1>Search</h1>
                <div>
                    <h2>Search For Username</h2>
                    <div>
                        <input type="text" id="searchUsername" name="searchUsername"
                        value={searchUsername} onChange={(e) => setSearchUsername(e.target.value)} required/>
                        <button onClick={search}>Search</button>
                    </div>
                </div>
            </section>
            <section className="search-content">
                {formatResults()}
            </section>

        </>
    )
}

export default Search
