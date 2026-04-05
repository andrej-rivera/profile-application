package com.andre.spring_react_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.andre.spring_react_project.model.LoginRequest;
import com.andre.spring_react_project.model.ProfileEntity;
import com.andre.spring_react_project.model.UserEntity;
import com.andre.spring_react_project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/admin/dump-users")
    public ResponseEntity<List<UserEntity>> dumpUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/admin/bulk-register")
    public ResponseEntity<List<UserEntity>> bulkRegister(@RequestBody List<UserEntity> users) {
        try {
            return ResponseEntity.ok(userService.bulkRegister(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/user/search")
    public ResponseEntity<List<String>> searchForUser(String username, int i, int p) {
        return ResponseEntity.ok(userService.searchForUser(username, i, p));
    }

    // Obtain the profile of either the currently authenticated user or any user by username.
    // Use /user/profile for the current session user, or /user/profile/{username} for a specific user.
    @GetMapping({"/user/profile", "/user/profile/{username}"})
    public ResponseEntity<Object> getUserProfile(@PathVariable(required = false) String username) {
        try {
            return ResponseEntity.ok(userService.getProfile(username));  
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find profile?: "+ e.getMessage());
        }
    }

    // Set the profile of the currently authenticated user given their username and the new profile information
    @PostMapping("/user/set-profile")
    public ResponseEntity<Object> setUserProfile(@RequestBody ProfileEntity profile) {
        try {
            return ResponseEntity.ok(userService.setProfile(profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Set Profile Failed huh?: "+ e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody UserEntity user) {
        UserEntity response = userService.register(user);
        return ResponseEntity.ok(response);
    }

    // custom login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest user, HttpServletRequest request) {
        try {

            // invalidate any existing session
            HttpSession currentSession = request.getSession(false);
            if(currentSession != null) {
                currentSession.invalidate();
            }

            // authenticate the user using the provided username and password
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authResult = authenticationManager.authenticate(authenticationToken);


            // create new session  
            HttpSession newSession = request.getSession(true);

            // set the authentication in the security context and associate it with the new session
            SecurityContextHolder.getContext().setAuthentication(authResult);
            newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok("Login for " + authentication.getName() + " successful :DDDDD");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed >:(. FORGOT YOUR PASSWORD!? "+ e.getMessage());
        }
    }


}
