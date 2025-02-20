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
    private String nameTest = "Deino Dog";
    private String birthdayTest = "2020-07-20";
    private String emailTest = "deino@dog.com";
    private String usernameTest = "deinodog";
    private String passwordTest = "treats123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User(nameTest, birthdayTest, emailTest, usernameTest, passwordTest);
        sampleUser.setId(1L);
    }

    @Test
    void createNewUserTest() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        User createdUser = userService.createNewUser(sampleUser);
        assertNotNull(createdUser);
        assertEquals("Deino Dog", createdUser.getName());
        assertEquals("deino@dog.com", createdUser.getEmail());
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
        assertEquals("Deino Dog", userFound.getName());
    }

    @Test
    void updateUserTest() {
        sampleUser.setName("Updated Dog");
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        User updatedUser = userService.updateUser(1L, sampleUser);
        assertNotNull(updatedUser);
        assertEquals("Updated Dog", updatedUser.getName());
    }
}
