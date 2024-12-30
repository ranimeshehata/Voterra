package com.voterra.services;
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
            user.setSavedPosts(new ArrayList<>()); // Initialize if null
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
  
  public List<Post> getPaginatedPosts(int page) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

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

    public List<Post> getSavedPosts(String email, int page) {
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

        User user = userRepository.findByEmail(email);
        List<String> savedPostIds = user.getSavedPosts();

        return postRepository.findByIdIn(savedPostIds,pageable);
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

    public void reportPost(ReportedPost reportedPost) {
        ReportedPost rp = reportedPostRepository.findById(reportedPost.getPostId()).orElse(null);
        if (rp == null) {
            reportedPostRepository.save(reportedPost);
        }
        else{
            String reporterId = reportedPost.getReportersId().getFirst();
            if (rp.getReportersId().contains(reporterId)) {
                return;
            }
            rp.getReportersId().add(reporterId);
        }
    }

    public List<Post> getReportedPosts() {
        List<ReportedPost> reportedPosts = reportedPostRepository.findAll();
        List<Post> originalPosts = new ArrayList<>();

        for (ReportedPost reportedPost : reportedPosts) {
            Post post = postRepository.findById(reportedPost.getPostId()).orElse(null);
            if (post != null) {
                originalPosts.add(post);
            }
        }
        return originalPosts;
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
            }

            postRepository.deleteById(postId);
            reportedPostRepository.deleteById(postId);
        }
    }

    public void leaveReportedPost(String postId) {
        ReportedPost reportedPost = reportedPostRepository.findById(postId).orElse(null);
        if (reportedPost != null) {
            reportedPostRepository.deleteById(postId);
        }
    }

}
