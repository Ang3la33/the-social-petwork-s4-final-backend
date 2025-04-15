package com.socialpetwork.controller;

import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.exception.UserException;
import com.socialpetwork.repository.UserRepository;
import com.socialpetwork.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    private final User testUser = new User("Angela", "angela@email.com", "angie", "secret", UserType.USER, "about me", "2000-01-01", null);

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        when(userService.createNewUser(any(User.class))).thenReturn(testUser);

        ResponseEntity<?> response = userController.registerUser(testUser);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testRegisterUser_Failure() {
        when(userService.createNewUser(any(User.class))).thenThrow(new UserException("Username exists"));
        ResponseEntity<?> response = userController.registerUser(testUser);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Username exists", response.getBody());
    }

}
