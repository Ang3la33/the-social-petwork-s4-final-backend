package com.socialpetwork.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.auth.jwt.JwtAuthFilter;
import com.socialpetwork.auth.login.LoginRequest;
import com.socialpetwork.entity.User;
import com.socialpetwork.exception.UserException;
import com.socialpetwork.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerUser_returnsCreatedUser() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("username");
        savedUser.setPassword(null);

        Mockito.when(userService.createNewUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"));

    }

    @Test
    void loginUser_withCorrectCredentials_returnsOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword(new BCryptPasswordEncoder().encode("password123"));

        Mockito.when(userService.getUserFromUsername("testuser")).thenReturn(mockUser);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful. User ID: " + mockUser.getId()));

    }

    @Test
    void registerUser_withDuplicateUsername_returnsBadRequest() throws Exception {
        User user = new User();
        user.setUsername("duplicateUsername");
        user.setPassword("password123");

        Mockito.when(userService.createNewUser(any(User.class)))
                .thenThrow(new UserException("Username already exists"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void loginUser_withIncorrectPassword_returnsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongPassword");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword(new BCryptPasswordEncoder().encode("correctPassword"));

        Mockito.when(userService.getUserFromUsername("testuser")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password."));
    }

}
