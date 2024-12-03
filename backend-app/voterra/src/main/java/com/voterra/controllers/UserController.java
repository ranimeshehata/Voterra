package com.voterra.controllers;
import com.voterra.entities.User;
import com.voterra.tokenization.JwtResponse;
import com.voterra.tokenization.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.voterra.repos.UserRepository;
import com.voterra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
            Object[] newUser = userService.signup(user);
            return ResponseEntity.ok(new JwtResponse(newUser));
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }
  
    @PostMapping("/signupWithFacebook")
    public ResponseEntity<?> signupWithFacebook(@RequestBody User user) {
        try {
            Object[] userToken = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(userToken));
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }
  
     @PostMapping("/signupWithGoogle")
     public ResponseEntity<?> signupWithGoogle(@RequestBody User user) {
        try {
            Object[] userToken = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(userToken));
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
     }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object[] token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }


    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userService.signOut();
        return ResponseEntity.ok(Map.of("message", "Signed out successfully"));
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody User user) {
        System.out.println(user);
        try {
            Object[] token = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }

    @PostMapping("/loginWithFacebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody User user) {
        try {
            Object[] token = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody LoginRequest forgetPasswordRequest) {
        try {
            boolean response = userService.forgetPassword(forgetPasswordRequest.getEmail(), forgetPasswordRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }
}
