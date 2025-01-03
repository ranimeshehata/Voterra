package com.voterra.controllers;

import com.voterra.entities.Story;
import com.voterra.repos.StoryRepository;
import com.voterra.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping("/stories")
public class StoryController {
    @Autowired
    private StoryService storyService;
    @Autowired
    private StoryRepository storyRepository;


    @GetMapping("/homepage")
    public Map<String, List<Story>> getStoriesGroupedByUser() {
        return storyService.getStoriesForHomepageGroupedByUser();
    }

    @GetMapping("/userStories")
    public ResponseEntity<?> getUserStories(@RequestParam String email) {
        try {
            return ResponseEntity.ok(storyService.getUserStories(email));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
}
