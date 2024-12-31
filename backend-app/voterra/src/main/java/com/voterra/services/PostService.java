package com.voterra.services;
import com.voterra.entities.Poll;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.repos.PostRepository;
import com.voterra.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.voterra.tokenization.JwtUtils;



import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository ;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
