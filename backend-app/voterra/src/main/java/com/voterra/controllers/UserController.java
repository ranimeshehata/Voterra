package com.voterra.controllers;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import com.voterra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    private UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            User newUser = userService.signup(user);
            return ResponseEntity.ok("User created successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage()); // Return error message if account already exists
        }
    }
}
