package com.andre.spring_react_project.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.andre.spring_react_project.model.UserEntity;

// CustomUserDetails implements UserDetails and wraps around UserEntity to provide user information to Spring Security
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;
    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert comma-separated roles into a list of GrantedAuthority
        return Arrays.stream(user.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());
    }
 
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
}
