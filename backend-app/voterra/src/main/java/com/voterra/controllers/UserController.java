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
            return ResponseEntity.status(400).body(e.getMessage()); // Return error message if account already exists
        }
    }
  
    @PostMapping("/signupWithFacebook")
    public ResponseEntity<?> signupWithFacebook(@RequestBody User user) {
        try {
            Object[] userToken = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(userToken));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
  
     @PostMapping("/signupWithGoogle")
     public ResponseEntity<?> signupWithGoogle(@RequestBody User user) {
        try {
            Object[] userToken = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(userToken));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
     }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object[] token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userService.signOut();
        return ResponseEntity.ok("User signed out successfully!");
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody User user) {
        try {
            Object[] token = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/loginWithFacebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody User user) {
        try {
            Object[] token = userService.signupOrLoginWithGoogleOrFacebook(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
