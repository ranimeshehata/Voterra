package com.voterra.controllers;

import com.voterra.services.PostService;
import com.voterra.repos.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    private PostRepository postRepository;

    @GetMapping("/homepage")
    public ResponseEntity<?> getPosts(
            @RequestParam int page) {
        try {
            return ResponseEntity.ok(postService.getPaginatedPosts(page));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
}
