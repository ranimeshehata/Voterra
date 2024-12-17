package com.voterra.services;

import com.voterra.entities.Story;
import com.voterra.repos.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoryService {
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private UserService userService;


    public Map<String, List<Story>> getStoriesForHomepageGroupedByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Story> userStories = storyRepository.findByUserEmailOrderByPublishedDateAsc(email);

        List<String> friends = userService.getFriends(email);
        List<Story> friendStories = storyRepository.findFriendStoriesOrderByPublishedDateAsc(friends);

        List<Story> allStories = new ArrayList<>();
        allStories.addAll(userStories);
        allStories.addAll(friendStories);

        allStories.sort((s1, s2) -> s2.getPublishedDate().compareTo(s1.getPublishedDate()));

        Map<String, List<Story>> groupedStories = new HashMap<>();
        for (Story story : allStories) {
            groupedStories.computeIfAbsent(story.getUserEmail(), k -> new ArrayList<>()).add(story);
        }

        return groupedStories;
    }
    public List<Story> getUserStories(String email) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.equals(email)) {
            return storyRepository.findByUserEmailOrderByPublishedDateAsc(email);
        }
        else if(userService.getFriends(user).contains(email)) {
            return storyRepository.findFriendStoriesOrderByPublishedDateAsc(List.of(email));
        }
        else {
            return storyRepository.findNonFriendStoriesOrderByPublishedDateAsc(email);
        }
    }
}
