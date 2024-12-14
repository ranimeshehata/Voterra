package com.voterra.services;
import com.voterra.entities.Post;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository ;
    public Post createPost(Post post){
        return postRepository.save(post) ;
    }
    public boolean deletePostById(String id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        } else {
            throw new PostNotFoundException(id);
        }
    }
}
