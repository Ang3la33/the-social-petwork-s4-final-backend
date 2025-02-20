// NOT WORKING
package com.socialpetwork.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Junit test
// create user? update? getters and setters?
public class UserServiceTest {
    private User user;
    private final String nameTest = "Deino Dog";
    private final String birthdayTest = "2020-07-20";
    private final String emailTest = "deino@dog.com";
    private final String usernameTest = "deinodog";
    private final String passwordTest = "treats123";

    @BeforeEach
    void setUp(){
        user = new User(nameTest, birthdayTest, emailTest, usernameTest, passwordTest);
    }

    @Test
    void constructorTest(){
        User newUser = new User();
        assertNotNull(newUser);
        assertNull(newUser.getName());
        assertNull(newUser.getBirthday());
        assertNull(newUser.getEmail());
        assertNull(newUser.getUsername());
        assertNull(newUser.getPassword());
    }



}
