package com.andre.spring_react_project.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.andre.spring_react_project.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable()) // Disable CSRF for simplicity (enable lator?)
            .cors(cors -> cors.configurationSource(corsConfigrationSource())) // Enable CORS w/ custom configuration (customize later)
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/login").permitAll() // Allow unauthenticated access to the login endpoint
                .requestMatchers("/register").permitAll() // Allow unauthenticated access to the registration endpoint
                .requestMatchers("/admin/**", "/admin/dump-users").hasRole("ADMIN") // Only allow users with the ADMIN role to access admin endpoints
                .anyRequest().authenticated())
            .exceptionHandling((exceptions) ->
                exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // Return 401 Unauthorized for unauthenticated requests instead of redirecting to a login page (since this is an API backend, not a traditional web app)
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value())) // on successful logout, return 200 OK instead of redirecting to a login page (since this is an API backend, not a traditional web app)
                .permitAll()); // Configure logout to delete session cookie and invalidate session
            return http.build();
    }

    // Configure the AuthenticationManager to use our CustomUserDetailsService, PasswordEncoder, and CustomerAuthenticationProvider
    // Note: We create a DaoAuthenticationProvider rather than AuthenticationManagerBuilder to avoid circular dependency issues with the CustomAuthenticationProvider (causes infinite loop on failed authenticate)
    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoProvider);
    }


    @Bean
    @Primary
    public CustomUserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Setup CORS (Cross-Origin Resource Sharing) to allow requests from the React frontend
    // This is a basic configuration that allows all origins, methods, and headers. This is a new thing compared to the last time I set up Spring Security, so I will need to test and adjust this as needed.
    @Bean
    public CorsConfigurationSource corsConfigrationSource() {
        // Configure CORS settings to align w/ React frontend
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow requests from the React frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allow HTTP merthods
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies)
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        

        // Register the CORS configuration for all endpoints (frontend can access all backend endpoints)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
