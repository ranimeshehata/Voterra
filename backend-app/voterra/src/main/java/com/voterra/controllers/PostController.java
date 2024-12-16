package com.voterra.controllers;
import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.UserRepository;
import com.voterra.services.PostService;
import com.voterra.tokenization.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import static com.voterra.services.UserService.isAdmin;


@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/createPost")
    public ResponseEntity<?>  createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @DeleteMapping("/deletePost")
    public ResponseEntity<?> deletePost(@RequestBody Post post) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail);

        if (!(post.getUserEmail().equals(userEmail) || isAdmin(user))) { // if not the creator of the post or an admin or a superadmin he can not delete the post
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this post");
        }
        try {
            postService.deletePostById(post.getId());
            return ResponseEntity.ok("post deleted successfully");
        } catch (PostNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/savePost")
    public ResponseEntity<?> savePost(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String postId = request.get("postId");
            postService.savePost(email, postId);
            return ResponseEntity.ok("post saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

}
