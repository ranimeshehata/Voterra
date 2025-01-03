package com.voterra.services;
import com.voterra.DTOs.ReportedPostDTO;
import com.voterra.entities.*;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.PostRepository;
import com.voterra.repos.ReportedPostRepository;
import com.voterra.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.voterra.tokenization.JwtUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository ;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportedPostRepository reportedPostRepository;

    private final JwtUtils jwtUtils = new JwtUtils();
    public Post createPost(Post post){
        return postRepository.save(post) ;
    }
    public void deletePostById(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new PostNotFoundException(id);
        }
    }

    public void savePost(String userEmail, String postId){
        User user = userRepository.findByEmail(userEmail);
        if (user.getSavedPosts() == null) {
            user.setSavedPosts(new ArrayList<>());
        }
        if(postRepository.existsById(postId)){
            if(!user.getSavedPosts().contains(postId)){
                user.getSavedPosts().add(postId);
                userRepository.save(user);
            }
        }
        else{
            throw new PostNotFoundException(postId);
        }
    }

    public void vote(String userEmail, String postId, int pollIndex){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<Poll> polls = post.getPolls();
        List<String> voters = polls.get(pollIndex).getVoters();
        if(!voters.contains(userEmail)){
            voters.add(userEmail);
            postRepository.save(post);
        }
    }
  
  public List<Post> getPaginatedPosts(String category, int page){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));
        if(category.equals("all")) {
            List<Post> userPosts = postRepository.findByUserEmail(email, pageable);

            List<String> friends = userService.getFriends(email);
            List<Post> friendPosts = postRepository.findByUserEmailInWithSpecificPrivacy(friends, pageable);

            List<String> excludedEmails = new ArrayList<>(friends);
            excludedEmails.add(email);
            List<Post> nonFriendPosts = postRepository.findByUserEmailNotInWithPublicPrivacy(excludedEmails, pageable);
            List<Post> combinedPosts = new ArrayList<>();
            combinedPosts.addAll(userPosts);
            combinedPosts.addAll(friendPosts);
            combinedPosts.addAll(nonFriendPosts);
            combinedPosts.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));
            return combinedPosts;
        }
        else {
            List<Post> userPosts = postRepository.findByUserEmailAndCategory(email, category, pageable);

            List<String> friends = userService.getFriends(email);
            List<Post> friendPosts = postRepository.findByUserEmailInWithSpecificPrivacyAndCategory(friends, category, pageable);

            List<String> excludedEmails = new ArrayList<>(friends);
            excludedEmails.add(email);
            List<Post> nonFriendPosts = postRepository.findByUserEmailNotInWithPublicPrivacyAndCategory(excludedEmails, category, pageable);
            List<Post> combinedPosts = new ArrayList<>();
            combinedPosts.addAll(userPosts);
            combinedPosts.addAll(friendPosts);
            combinedPosts.addAll(nonFriendPosts);
            combinedPosts.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));
            return combinedPosts;
        }
    }


    public List<Post> getSavedPosts(String email, int page) {
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

        User user = userRepository.findByEmail(email);
        List<String> savedPostIds = user.getSavedPosts();
        for(String postId : savedPostIds) {
            if (!postRepository.existsById(postId)) {
                savedPostIds.remove(postId);
            }
        }
        user.setSavedPosts(savedPostIds);
        userRepository.save(user);
        return postRepository.findByIdIn(savedPostIds, pageable);
    }

    public List<Post> getUserPosts(String email, int page) {
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.equals(email)) {
            return postRepository.findByUserEmail(email, pageable);
        }
        else if(userService.getFriends(user).contains(email)) {
            return postRepository.findByUserEmailInWithSpecificPrivacy(List.of(email), pageable);
        }
        else {
            return postRepository.findByUserEmailWithPublicPrivacy(email, pageable);
        }
    }

    public String reportPost(ReportedPost reportedPost) {
        ReportedPost rp = reportedPostRepository.findById(reportedPost.getPostId()).orElse(null);
        if (rp == null) {
            reportedPostRepository.save(reportedPost);
            User user = userRepository.findByEmail(reportedPost.getReportersId().getFirst());
            user.getReportedPosts().add(reportedPost.getPostId());
            userRepository.save(user);
        }
        else{
            String reporterId = reportedPost.getReportersId().getFirst();
            if (rp.getReportersId().contains(reporterId)) {
                return "You have already reported this post";
            }
            rp.getReportersId().add(reporterId);
            User user = userRepository.findByEmail(reportedPost.getReportersId().getFirst());
            user.getReportedPosts().add(reportedPost.getPostId());
            userRepository.save(user);
            reportedPostRepository.save(rp);
        }
        return "Post reported successfully";
    }

    public List<ReportedPostDTO> getReportedPosts(int page) {
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size);
        List<ReportedPost> reportedPosts = reportedPostRepository.findAll(pageable).getContent();
        List<ReportedPostDTO> reportedPostDTOS = new ArrayList<>();
        for (ReportedPost reportedPost : reportedPosts) {
            postRepository.findById(reportedPost.getPostId())
                    .ifPresent(post -> reportedPostDTOS.add(new ReportedPostDTO(post, reportedPost.getReportersId().size())));
        }
        return reportedPostDTOS;
    }

    public void deleteReportedPost(String postId) {
        ReportedPost reportedPost = reportedPostRepository.findById(postId).orElse(null);
        if (reportedPost != null) {

            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.getSavedPosts() != null && user.getSavedPosts().contains(postId)) {
                    user.getSavedPosts().remove(postId);
                    userRepository.save(user);
                }
                if (user.getReportedPosts() != null && user.getReportedPosts().contains(postId)) {
                    user.getReportedPosts().remove(postId);
                    userRepository.save(user);
                }
            }

            postRepository.deleteById(postId);
            reportedPostRepository.deleteById(postId);
        }
    }

    public void leaveReportedPost(String postId) {
        ReportedPost reportedPost = reportedPostRepository.findById(postId).orElse(null);
        if (reportedPost != null) {
            reportedPostRepository.deleteById(postId);
            List<User> users = userRepository.findAll();
            for (User user : users) {
                if (user.getReportedPosts() != null && user.getReportedPosts().contains(postId)) {
                    user.getReportedPosts().remove(postId);
                    userRepository.save(user);
                }
            }
        }
    }

    public List<Post> searchPosts(String postContent, int page) {
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));
        List<Post> matchedPost =  postRepository.findByPostContentContaining(postContent, pageable);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> friends = userService.getFriends(email);
        List<Post> returnedPost = new ArrayList<>();
        for(Post post : matchedPost){
            if(String.valueOf(post.getPrivacy()).equals("PUBLIC")){
                returnedPost.add(post);
            }
            else if(String.valueOf(post.getPrivacy()).equals("FRIENDS") && friends.contains(post.getUserEmail())){
                returnedPost.add(post);
            }
            else if(post.getUserEmail().equals(email)){
                returnedPost.add(post);
            }
        }
        returnedPost.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));
        return returnedPost;
    }

}
