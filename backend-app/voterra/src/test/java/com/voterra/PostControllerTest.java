package com.voterra;

import com.voterra.controllers.PostController;
import com.voterra.entities.Post;
import com.voterra.services.PostService;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetPosts() {
        int page = 0;
        Post post = new Post("testuser@example.com","testuser" ,"Test Post", null, Post.privacy.PUBLIC, null, new Date());
        when(postService.getPaginatedPosts(page)).thenReturn(Collections.singletonList(post));

        ResponseEntity<?> response = postController.getPosts(page);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertTrue(body.contains(post));
    }

    @Test
    void testGetPosts_ExceptionHandling() {
        int page = 0;
        when(postService.getPaginatedPosts(page)).thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<?> response = postController.getPosts(page);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Test exception", body.get("message"));
    }

    @Test
    void testGetSavedPosts_Success() {
        String email = "testuser@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        Post post = new Post(email, "username","Saved Post", null, Post.privacy.PUBLIC, null, new Date());
        when(postService.getSavedPosts(email, 0)).thenReturn(Collections.singletonList(post));

        ResponseEntity<?> response = postController.getSavedPosts(0);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List);
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
        assertEquals(post, body.get(0));

    }

    @Test
    void testGetSavedPosts_Exception() {
        String email = "testuser@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        when(postService.getSavedPosts(email, 0)).thenThrow(new RuntimeException("Error retrieving saved posts"));

        ResponseEntity<?> response = postController.getSavedPosts(0);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Error retrieving saved posts", body.get("message"));
    }
}
