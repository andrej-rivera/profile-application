import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router'
import './index.css'
import App from './App.jsx'
import Login from './Login.jsx'
import Dashboard from './Dashboard.jsx'
import Signup from './Signup.jsx'
import Search from './Search.jsx'
import CheckAuth from './CheckAuth.jsx'
import SetProfile from './SetProfile.jsx'
import Profile from './Profile.jsx'

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
    <Routes>
      <Route index element={<App />} />
      <Route path="login" element={<Login />} />
      <Route path="dashboard" element={<CheckAuth><Dashboard /></CheckAuth>} />
      <Route path="*" element={<h1>404 - Page Not Found</h1>} />
      <Route path="signup" element={<Signup/>} />
      <Route path="search" element={<CheckAuth><Search/></CheckAuth>} />
      <Route path="set-profile" element={<CheckAuth><SetProfile/></CheckAuth>} />
      <Route path="profile/:username" element={<CheckAuth><Profile/></CheckAuth>} />

    </Routes>
  </BrowserRouter>,
)
