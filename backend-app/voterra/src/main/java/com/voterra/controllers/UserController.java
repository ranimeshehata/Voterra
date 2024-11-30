package com.voterra.controllers;

import com.voterra.entities.User;
import com.voterra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signupWithGoogle")
    public ResponseEntity<?> signupWithGoogle(@RequestBody User user) {
        try {
            User newUser = userService.signupWithGoogleOrFacebook(user);
            return ResponseEntity.ok(newUser);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signupWithFacebook")
    public ResponseEntity<?> signupWithFacebook(@RequestBody User user) {
        try {
            User newUser = userService.signupWithGoogleOrFacebook(user);
            return ResponseEntity.ok(newUser);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
