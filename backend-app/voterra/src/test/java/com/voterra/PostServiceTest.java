package com.voterra;

import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.repos.PostRepository;
import com.voterra.repos.UserRepository;
import com.voterra.services.PostService;
import com.voterra.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPaginatedPosts() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);

        Post userPost = new Post("testuser@example.com","testuser" ,"User Post", null, Post.privacy.PUBLIC, null, new Date());
        Post friendPost = new Post("friend@example.com", "testuser" ,"Friend Post", null, Post.privacy.FRIENDS, null, new Date());
        Post nonFriendPost = new Post("nonfriend@example.com", "testuser" ,"Non-Friend Post", null, Post.privacy.PUBLIC, null, new Date());

        when(postRepository.findByUserEmail(eq(currentUserEmail), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(userPost));

        when(userService.getFriends(currentUserEmail))
                .thenReturn(Collections.singletonList("friend@example.com"));

        when(postRepository.findByUserEmailInWithSpecificPrivacy(eq(List.of("friend@example.com")), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(friendPost));

        when(postRepository.findByUserEmailNotInWithPublicPrivacy(eq(Arrays.asList("friend@example.com", "testuser@example.com")), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(nonFriendPost));

        List<Post> result = postService.getPaginatedPosts(0);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(userPost));
        assertTrue(result.contains(friendPost));
        assertTrue(result.contains(nonFriendPost));
    }

    @Test
    void testGetSavedPosts() {
        String email = "testuser@example.com";
        int page = 0;
        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedDate"));

        User user = new User();
        user.setEmail(email);
        user.setSavedPosts(List.of("post1", "post2"));

        Post post1 = new Post("user1@example.com","user1" ,"Post 1", null, Post.privacy.PUBLIC, null, new Date());
        post1.setId("post1");
        Post post2 = new Post("user2@example.com", "user2","Post 2", null, Post.privacy.PUBLIC, null, new Date());
        post2.setId("post2");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(postRepository.findByIdIn(user.getSavedPosts(), pageable)).thenReturn(List.of(post1, post2));

        List<Post> result = postService.getSavedPosts(email, page);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(post1));
        assertTrue(result.contains(post2));
    }
}