package com.andre.spring_react_project.model;


// For handling login requests (username and password)
public class LoginRequest {
    private String username;
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }    
}
