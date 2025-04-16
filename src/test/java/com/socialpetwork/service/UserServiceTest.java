package com.socialpetwork.service;

import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.exception.UserException;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable closeable;
    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("Test Name", "test@email.com", "testuser", "password", UserType.USER, "About", "2000-01-01", null);
        user.setId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void createNewUser_success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createNewUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void createNewUser_usernameExists_throwsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(UserException.class, () -> userService.createNewUser(user));
    }

    @Test
    void createNewUser_emailExists_throwsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        assertThrows(UserException.class, () -> userService.createNewUser(user));
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserFromId(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        User result = userService.getUserFromUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

}
