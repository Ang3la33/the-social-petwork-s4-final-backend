package com.socialpetwork.auth.config;

import com.socialpetwork.auth.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Apply security settings to incoming http requests
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // disable csrf (cross-site reference forgery) as this is a stateless app using JWT tokens not cookies
                .csrf(csrf -> csrf.disable())
                // don't create a session / store any user info on the server between requests
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // no login/token needed for accessing the login and registration endpoints / all other routes require authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/login", "/users/register", "/posts/**").permitAll()
                        .anyRequest().authenticated()
                )
                // run custom JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
