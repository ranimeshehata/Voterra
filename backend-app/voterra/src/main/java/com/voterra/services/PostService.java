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
//        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
//        System.out.println(post.getId());
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
            //print voters
            System.out.println(Arrays.toString(voters.toArray()));
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

}
