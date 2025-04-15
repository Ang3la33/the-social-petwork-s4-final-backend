package com.socialpetwork.controller;

import com.socialpetwork.auth.jwt.JwtUtil;
import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.exception.UserException;
import com.socialpetwork.repository.UserRepository;
import com.socialpetwork.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MultipartFile mockFile;

    private AutoCloseable closeable;
    private User user;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("Angela", "angela@email.com", "angie", "secret", UserType.USER, "about", "2000-01-01", null);
        user.setId(1L);
    }

    @Test
    void registerUser_success() {
        when(userService.createNewUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void registerUser_conflict() {
        when(userService.createNewUser(any(User.class)))
                .thenThrow(new UserException("Email already exists"));

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void loginUser_success() {
        when(userService.getUserFromUsername("angie")).thenReturn(user);
        when(passwordEncoder.matches("secret", "secret")).thenReturn(true);
        when(jwtUtil.generateToken("angie", UserType.USER)).thenReturn("test-token");

        Map<String, String> loginData = Map.of("username", "angie", "password", "secret");
        ResponseEntity<?> response = userController.loginUser(loginData);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("test-token", responseBody.get("token"));
        assertEquals("angie", responseBody.get("username"));
        assertEquals("USER", responseBody.get("role"));
    }

    @Test
    void loginUser_invalidCredentials() {
        when(userService.getUserFromUsername("angie")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "secret")).thenReturn(false);

        Map<String, String> loginData = Map.of("username", "angie", "password", "wrong");
        ResponseEntity<?> response = userController.loginUser(loginData);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void getAllUsers_returnsUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getUserById_found() {
        when(userService.getUserFromId(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_notFound() {
        when(userService.getUserFromId(1L)).thenReturn(null);

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateUser_success() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.updateUser(1L, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void deleteUser_adminSuccess() {
        when(jwtUtil.validateToken("validToken")).thenReturn(true);
        when(jwtUtil.getRoleFromToken("validToken")).thenReturn("ADMIN");
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser(1L, "Bearer validToken");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_notAdmin() {
        when(jwtUtil.validateToken("token")).thenReturn(true);
        when(jwtUtil.getRoleFromToken("token")).thenReturn("USER");

        ResponseEntity<?> response = userController.deleteUser(1L, "Bearer token");

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void getPostsByUser_notFound() {
        when(userService.getPostsByUser(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = userController.getPostsByUser(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}

