package com.voterra.controllers;
import com.voterra.entities.Admin;
import com.voterra.entities.Post;
import com.voterra.entities.ReportedPost;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.UserRepository;
import com.voterra.repos.PostRepository;
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
    private PostRepository postRepository;


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

    @GetMapping("/getSavedPosts")
    public ResponseEntity<?> getSavedPosts(@RequestParam int page) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(postService.getSavedPosts(email , page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
  
    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String postId = request.get("postId");
            int pollIndex = Integer.parseInt(request.get("pollIndex"));
            postService.vote(email, postId, pollIndex);
            return ResponseEntity.ok("voted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

   @GetMapping("/homepage")
    public ResponseEntity<?> getPosts(
            @RequestParam String category, @RequestParam int page) {
        try {
            return ResponseEntity.ok(postService.getPaginatedPosts(category, page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/userContent")
    public ResponseEntity<?> getUserContent(
            @RequestParam String email ,
            @RequestParam int page) {
        try {
            return ResponseEntity.ok(postService.getUserPosts(email , page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/reportPost")
    public ResponseEntity<?> reportPost(@RequestBody ReportedPost reportedPost) {
        try {
            return ResponseEntity.ok(postService.reportPost(reportedPost));

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String postContent,
            @RequestParam int page) {
        try {
            return ResponseEntity.ok(postService.searchPosts(postContent, page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping("/getReportedPosts")
    public ResponseEntity<?> getReportedPosts(@RequestParam int page) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email);
            if (user.getUserType() != User.userType.ADMIN) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this post");
            }
            return ResponseEntity.ok(postService.getReportedPosts(page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteReportedPost")
    public ResponseEntity<?> deleteReportedPost(@RequestBody Map<String, String> request) {
        try {
            String postId = request.get("postId");
            String email = request.get("email");
            User user = userRepository.findByEmail(email);
            if (user.getUserType() != User.userType.ADMIN) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this post");
            }
            postService.deleteReportedPost(postId);
            return ResponseEntity.ok("post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/leaveReportedPost")
    public ResponseEntity<?> leaveReportedPost(@RequestBody Map<String, String> request) {
        try {
            String postId = request.get("postId");
            String email = request.get("email");
            User user = userRepository.findByEmail(email);
            if (user.getUserType() != User.userType.ADMIN) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this post");
            }
            postService.leaveReportedPost(postId);
            return ResponseEntity.ok("post deleted successfully from reported posts list");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

}
