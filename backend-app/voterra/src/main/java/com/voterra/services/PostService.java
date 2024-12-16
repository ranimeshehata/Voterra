package com.voterra.services;

import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.repos.PostRepository;
import com.voterra.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    private UserRepository userRepository;
    private UserService userService;


    public List<Post> getPaginatedPosts(int page) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

        List<Post> userPosts = postRepository.findByUserEmail(email, pageable);
        System.out.println("done1");
        List<String> friends = userService.getFriends(email);
        List<Post> friendPosts = postRepository.findByUserEmailInWithSpecificPrivacy(friends, pageable);
        System.out.println("done2");
        List<String> excludedEmails = new ArrayList<>(friends);
        excludedEmails.add(email);
        List<Post> nonFriendPosts = postRepository.findByUserEmailNotInWithPublicPrivacy(excludedEmails, pageable);
        System.out.println("done3");
        List<Post> combinedPosts = new ArrayList<>();
        combinedPosts.addAll(userPosts);
        combinedPosts.addAll(friendPosts);
        combinedPosts.addAll(nonFriendPosts);
        combinedPosts.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));

        return combinedPosts;
    }

}
