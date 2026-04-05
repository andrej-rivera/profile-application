package com.andre.spring_react_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// Contains user profile information (e.g., name, bio, profile picture URL)
@Entity
public class ProfileEntity {
    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;

    public ProfileEntity() {
    }

    public ProfileEntity(long id, String firstName, String lastName, String bio, String profilePictureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}
