package com.socialpetwork.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    private final String nameTest = "Deino Dog";
    private final String birthdayTest = "2020-07-20";
    private final String emailTest = "deino@dog.com";
    private final String usernameTest = "deinodog";
    private final String passwordTest = "treats123";
    private final String profileUrlTest = "https://example.com/profile/deinodog";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sampleUser with all required fields
        sampleUser = new User(nameTest, birthdayTest, emailTest, usernameTest, passwordTest, profileUrlTest);
        sampleUser.setId(1L);
    }

    @Test
    void createNewUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User createdUser = userService.createNewUser(sampleUser);

        assertNotNull(createdUser);
        assertEquals(nameTest, createdUser.getName());
        assertEquals(emailTest, createdUser.getEmail());
        assertEquals(usernameTest, createdUser.getUsername());
        assertEquals(passwordTest, createdUser.getPassword());
        assertEquals(profileUrlTest, createdUser.getProfileUrl());

        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void deleteUserTest() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).delete(sampleUser);

        boolean isDeleted = userService.deleteUser(userId);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).delete(sampleUser);
    }

    @Test
    void findUserFromIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        User userFound = userService.getUserFromId(1L);

        assertNotNull(userFound);
        assertEquals(nameTest, userFound.getName());
        assertEquals(usernameTest, userFound.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUserTest() {
        sampleUser.setName("Updated Dog");
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User updatedUser = userService.updateUser(1L, sampleUser);

        assertNotNull(updatedUser);
        assertEquals("Updated Dog", updatedUser.getName());
        verify(userRepository, times(1)).save(sampleUser);
    }
}
