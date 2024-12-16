package com.voterra;

import com.voterra.entities.Post;
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

}