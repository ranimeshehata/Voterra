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
    private StoryRepository storyRepository;


    @GetMapping("/homepage")
    public Map<String, List<Story>> getStoriesGroupedByUser() {
        return storyService.getStoriesForHomepageGroupedByUser();
    }
}
