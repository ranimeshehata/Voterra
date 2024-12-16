package com.voterra;

import com.voterra.controllers.PostController;
import com.voterra.entities.Post;
import com.voterra.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        // Verify
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Test exception", body.get("message"));
    }

    @Test
    void testGetUserContent() {
        String email = "testuser@example.com";
        String username = "testuser";
        int page = 0;
        Post post = new Post(email, username ,"User Content", null, Post.privacy.PUBLIC, null, new Date());
        when(postService.getUserPosts(email, page)).thenReturn(Collections.singletonList(post));

        ResponseEntity<?> response = postController.getUserContent(email, page);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertTrue(body.contains(post));
    }

    @Test
    void testGetUserContent_ExceptionHandling() {
        String email = "testuser@example.com";
        int page = 0;
        when(postService.getUserPosts(email, page)).thenThrow(new RuntimeException("Test exception"));

        ResponseEntity<?> response = postController.getUserContent(email, page);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Test exception", body.get("message"));
    }
}
