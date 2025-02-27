package com.socialpetwork.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User createNewUser(@RequestBody User newUser) {
        System.out.println("Received POST request to create a new user: " + newUser);
        return userService.createNewUser(newUser);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserFromId(id);
    }

    @GetMapping("/users/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserFromUsername(username);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userInfo) {
        return userService.updateUser(id, userInfo);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
