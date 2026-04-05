package com.andre.spring_react_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String username;
    private String password;
    private String email;
    private String roles; // Comma-separated roles (e.g., "USER,ADMIN")

    // Getters and setters
    public long getId() {  return id; }   
    public String getUsername() {  return username; }   
    public String getPassword() {  return password; }   
    public String getEmail() {  return email; }
    public String getRoles() {  return roles; }

    public void setId(long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRoles(String roles) { this.roles = roles; }   

}
