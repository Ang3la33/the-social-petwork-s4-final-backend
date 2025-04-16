package com.socialpetwork.service;

import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

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

}
