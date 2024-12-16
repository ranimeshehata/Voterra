package com.voterra.services;
import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.PostRepository;
import com.voterra.repos.UserRepository;
import com.voterra.tokenization.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository ;

    @Autowired
    private UserRepository userRepository;
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



}
