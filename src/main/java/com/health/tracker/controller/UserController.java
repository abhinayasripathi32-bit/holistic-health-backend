package com.health.tracker.controller;

import com.health.tracker.model.User;
import com.health.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        User saved = userRepository.save(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        Optional<User> userOpt = userRepository.findByUsername(credentials.get("username"));
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(credentials.get("password"))) {
            User u = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", u.getId());
            response.put("username", u.getUsername());
            response.put("email", u.getEmail());
            response.put("fullName", u.getFullName());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(u -> { u.setPassword(null); return ResponseEntity.ok(u); })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id).map(existing -> {
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existing.setPassword(user.getPassword());
            }
            User saved = userRepository.save(existing);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted"));
        }
        return ResponseEntity.notFound().build();
    }
}
