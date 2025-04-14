package com.socialpetwork.controller;

import com.socialpetwork.auth.jwt.JwtUtil;
import com.socialpetwork.entity.User;
import com.socialpetwork.entity.Post;
import com.socialpetwork.exception.UserException;
import com.socialpetwork.repository.UserRepository;
import com.socialpetwork.service.UserService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        System.out.println("Received POST request to register a new user: " + newUser);
        try {
            User createdUser = userService.createNewUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userService.getUserFromUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getType());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getType().name(),
                "userId", user.getId()
        ));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserFromId(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserFromUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userInfo) {
        try {
            User updatedUser = userService.updateUser(id, userInfo);
            return ResponseEntity.ok(updatedUser);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String role = jwtUtil.getRoleFromToken(token);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid request. Only Admins can delete users");
        }

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        List<Post> posts = userService.getPostsByUser(userId);
        if (!posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile file) {
        try {
            System.out.println("📥 Uploading avatar for user ID: " + id);

            // Validate file type and size
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }

            if (file.getSize() > 5_000_000) {
                return ResponseEntity.badRequest().body("File is too large. Max 5MB");
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));

            // Delete old avatar
            String oldAvatarUrl = user.getAvatarUrl();
            if (oldAvatarUrl != null && !oldAvatarUrl.isEmpty()) {
                Path oldPath = Paths.get("uploads/avatars/" + oldAvatarUrl.replace("/avatars/", ""));
                Files.deleteIfExists(oldPath);
                System.out.println("🗑️ Deleted old avatar: " + oldAvatarUrl);
            }

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                    .orElse("jpg");

            List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "webp");
            if (!allowedExtensions.contains(extension.toLowerCase())) {
                return ResponseEntity.badRequest().body("Unsupported file type.");
            }

            String fileName = UUID.randomUUID().toString() + "." + extension;
            Path avatarDir = Paths.get("uploads/avatars/");
            Files.createDirectories(avatarDir);
            Path filePath = avatarDir.resolve(fileName);
            System.out.println("📂 Saving to: " + filePath.toAbsolutePath());

            try (InputStream in = file.getInputStream(); OutputStream out = Files.newOutputStream(filePath)) {
                Thumbnails.of(in)
                        .size(400, 400)
                        .outputFormat("jpg")
                        .outputQuality(0.8)
                        .toOutputStream(out);
            }

            user.setAvatarUrl("/avatars/" + fileName);
            System.out.println("✅ Avatar URL set: " + user.getAvatarUrl());

            userRepository.save(user);
            System.out.println("✅ User saved with new avatar!");

            return ResponseEntity.ok(Map.of(
                    "message", "Avatar uploaded successfully!",
                    "avatarUrl", user.getAvatarUrl()
            ));

        } catch (Exception e) {
            e.printStackTrace(); // Full error log
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while uploading avatar: " + e.getMessage());
        }
    }
}
