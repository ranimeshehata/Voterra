package com.voterra;

import com.voterra.controllers.PostController;
import com.voterra.entities.Post;
import com.voterra.entities.ReportedPost;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.UserRepository;
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

    @Mock
    private UserRepository userRepository;

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

    @Test
    void testCreatePost() {
        Post post = new Post("testuser@example.com", "testuser", "Test Post", null, Post.privacy.PUBLIC, null, new Date());
        when(postService.createPost(post)).thenReturn(post);

        ResponseEntity<?> response = postController.createPost(post);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(post, response.getBody());
    }

    @Test
    void testSavePost() {
        Map<String, String> request = Map.of("email", "testuser@example.com", "postId", "1");
        doNothing().when(postService).savePost("testuser@example.com", "1");

        ResponseEntity<?> response = postController.savePost(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("post saved successfully", response.getBody());
    }

    @Test
    void testVote() {
        Map<String, String> request = Map.of("email", "testuser@example.com", "postId", "1", "pollIndex", "0");
        doNothing().when(postService).vote("testuser@example.com", "1", 0);

        ResponseEntity<?> response = postController.vote(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("voted successfully", response.getBody());
    }

    @Test
    void testVote_ExceptionHandling() {
        Map<String, String> request = Map.of("email", "testuser@example.com", "postId", "1", "pollIndex", "0");
        doThrow(new RuntimeException("Voting error")).when(postService).vote("testuser@example.com", "1", 0);

        ResponseEntity<?> response = postController.vote(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Voting error", body.get("message"));
    }

    @Test
    void testSavePost_ExceptionHandling() {
        Map<String, String> request = Map.of("email", "testuser@example.com", "postId", "1");
        doThrow(new RuntimeException("Saving error")).when(postService).savePost("testuser@example.com", "1");

        ResponseEntity<?> response = postController.savePost(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Saving error", body.get("message"));
    }

    @Test
    void testDeletePost_Success() {
        String email = "testuser@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Post post = new Post(email, "testuser", "Test Post", null, Post.privacy.PUBLIC, null, new Date());
        post.setId(String.valueOf(1L));

        doNothing().when(postService).deletePostById(post.getId());

        ResponseEntity<?> response = postController.deletePost(post);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("post deleted successfully", response.getBody());
    }

    @Test
    void testDeletePost_Unauthorized() {
        String email = "testuser@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Post post = new Post("anotheruser@example.com", "anotheruser", "Test Post", null, Post.privacy.PUBLIC, null, new Date());
        post.setId(String.valueOf(1L));

        ResponseEntity<?> response = postController.deletePost(post);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("You are not authorized to delete this post", response.getBody());
    }

    @Test
    void testDeletePost_PostNotFoundException() {
        String email = "testuser@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Post post = new Post(email, "testuser", "Test Post", null, Post.privacy.PUBLIC, null, new Date());
        post.setId(String.valueOf(1L));

        doThrow(new PostNotFoundException("Post not found")).when(postService).deletePostById(post.getId());

        ResponseEntity<?> response = postController.deletePost(post);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        System.out.println(body.get("message"));
        assertEquals("Post with ID Post not found not found", body.get("message"));
    }

    @Test
    void testReportPost() {
        ReportedPost reportedPost = new ReportedPost();
        when(postService.reportPost(reportedPost)).thenReturn(reportedPost);

        ResponseEntity<?> response = postController.reportPost(reportedPost);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportedPost, response.getBody());
    }

    @Test
    void testReportPost_ExceptionHandling() {
        ReportedPost reportedPost = new ReportedPost();
        when(postService.reportPost(reportedPost)).thenThrow(new RuntimeException("Reporting error"));

        ResponseEntity<?> response = postController.reportPost(reportedPost);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Reporting error", body.get("message"));
    }

    @Test
    void testGetReportedPosts_Unauthorized() {
        String email = "user@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.USER);
        when(userRepository.findByEmail(email)).thenReturn(user);

        ResponseEntity<?> response = postController.getReportedPosts(0);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("You are not authorized to delete this post", response.getBody());
    }

    @Test
    void testGetReportedPosts_ExceptionHandling() {
        String email = "admin@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(user);

        when(postService.getReportedPosts(0)).thenThrow(new RuntimeException("Error retrieving reported posts"));

        ResponseEntity<?> response = postController.getReportedPosts(0);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Error retrieving reported posts", body.get("message"));
    }

    @Test
    void testDeleteReportedPost_Success() {
        String email = "admin@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);
        doNothing().when(postService).deleteReportedPost("1");

        ResponseEntity<?> response = postController.deleteReportedPost(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("post deleted successfully", response.getBody());
    }

    @Test
    void testDeleteReportedPost_Unauthorized() {
        String email = "user@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.USER);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);

        ResponseEntity<?> response = postController.deleteReportedPost(request);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("You are not authorized to delete this post", response.getBody());
    }

    @Test
    void testDeleteReportedPost_ExceptionHandling() {
        String email = "admin@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);
        doThrow(new RuntimeException("Error deleting reported post")).when(postService).deleteReportedPost("1");

        ResponseEntity<?> response = postController.deleteReportedPost(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Error deleting reported post", body.get("message"));
    }

    @Test
    void testLeaveReportedPost_Success() {
        String email = "admin@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);
        doNothing().when(postService).leaveReportedPost("1");

        ResponseEntity<?> response = postController.leaveReportedPost(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("post deleted successfully from reported posts list", response.getBody());
    }

    @Test
    void testLeaveReportedPost_Unauthorized() {
        String email = "user@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.USER);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);

        ResponseEntity<?> response = postController.leaveReportedPost(request);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("You are not authorized to delete this post", response.getBody());
    }

    @Test
    void testLeaveReportedPost_ExceptionHandling() {
        String email = "admin@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setUserType(User.userType.ADMIN);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> request = Map.of("postId", "1", "email", email);
        doThrow(new RuntimeException("Error leaving reported post")).when(postService).leaveReportedPost("1");

        ResponseEntity<?> response = postController.leaveReportedPost(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("Error leaving reported post", body.get("message"));
    }
}
