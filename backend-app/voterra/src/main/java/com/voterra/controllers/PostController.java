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
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/homepage")
    public ResponseEntity<?> getPosts(
            @RequestParam int page) {
        System.out.println("hiii");
        try {

            System.out.println(postService.getPaginatedPosts(page));
            return ResponseEntity.ok(postService.getPaginatedPosts(page));
        } catch (Exception e) {
            System.out.println("hi");
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
}
