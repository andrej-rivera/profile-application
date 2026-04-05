package com.andre.spring_react_project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andre.spring_react_project.model.ProfileEntity;
import com.andre.spring_react_project.model.UserEntity;
import com.andre.spring_react_project.repository.ProfileRepository;
import com.andre.spring_react_project.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Set the profile of the currently authenticated user given their username/authentication session
    // If the user doesn't have an existing profile, create a new one and associate it with the user.
    // Otherwise, simply update existing profile information with any new NON-NULL information provided in the request.
    public ProfileEntity setProfile(ProfileEntity profile) {

        // first, get the currently authenticated user
        // a user should only be able to set their own profile, so we ignore any username that may be passed in and instead get the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Current user not found (how did this happen?)");
        }
        
        // then, get the user's profile from the database (if it exists)
        ProfileEntity userProfile = profileRepository.findById(user.getId());

        // if it does exist, update the existing profile with any new information provided in the request
        if (profile.getFirstName() != null && !profile.getFirstName().isEmpty()) { userProfile.setFirstName(profile.getFirstName()); }
        if (profile.getLastName() != null && !profile.getLastName().isEmpty()) { userProfile.setLastName(profile.getLastName()); }
        if (profile.getBio() != null && !profile.getBio().isEmpty()) { userProfile.setBio(profile.getBio()); }
        if (profile.getProfilePictureUrl() != null && !profile.getProfilePictureUrl().isEmpty()) { userProfile.setProfilePictureUrl(profile.getProfilePictureUrl()); }

        return profileRepository.save(userProfile); // save updated profile to database and return it
    }

    // Get the profile of any user given their username.
    public ProfileEntity getProfile(String username) {
        // if no username provided, get the currently authenticated user's profile
        if (username == null || username.isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            username = authentication.getName();
        }

        // Find user by username, throw error if user doesn't exist
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        // Find profile by user ID (which is the same as profile ID), throw error if profile doesn't exist
        ProfileEntity profile = profileRepository.findById(user.getId());
        if (profile == null) {
            throw new RuntimeException("Profile not found for user: " + username + "  (tell them they should set their profile info!)");
        }

        return profile;

    }

    public List<String> searchForUser(String username, int count, int page) {
        return userRepository.findUsernamesStartingWith(username, PageRequest.of(page, count));
    }


    // Transactional to ensure both user and profile are created together, and to handle any potential rollbacks if something goes wrong during the registration process
    @Transactional
    public UserEntity register(UserEntity user) {
        // Quick check to see if the username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // otherwise, make new user and save to database first
        UserEntity u = new UserEntity();
        u.setUsername(user.getUsername());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        u.setEmail(user.getEmail()); // later, we add checks to ensure email is valid format
        u.setRoles(user.getRoles());

        UserEntity savedUser = userRepository.save(u);

        // also make new profile, linked to the saved user's generated id
        ProfileEntity p = new ProfileEntity(savedUser.getId(), "[First Name]", "[Last Name]", "Please update profile with new info", "https://www.pngkey.com/png/detail/233-2332677_image-500580-placeholder-transparent.png");
        profileRepository.save(p);

        return savedUser;
    } 
    
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public List<UserEntity> bulkRegister(List<UserEntity> users) {
        List<UserEntity> registered = new ArrayList<>();
        for (UserEntity user : users) {
            try {
                registered.add(register(user));
            } catch (Exception e) {
                // Log the error and skip this user
                System.err.println("Failed to register user: " + user.getUsername() + " - " + e.getMessage());
            }
        }
        return registered;
    }
}
