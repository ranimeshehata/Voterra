package com.voterra;

import com.voterra.entities.Poll;
import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
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

import java.util.*;

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
    public void testSavePostSuccess() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);
        user.setSavedPosts(new ArrayList<>()); // Initialize savedPosts

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(true);

        // Act
        postService.savePost(userEmail, postId);

        // Assert
        verify(userRepository, times(1)).save(user);
        assert user.getSavedPosts().contains(postId);
    }

    @Test
    public void testSavePostAlreadySaved() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);
        user.setSavedPosts(new ArrayList<>());
        user.getSavedPosts().add(postId); // Post already saved

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(true);

        // Act
        postService.savePost(userEmail, postId);

        // Assert
        verify(userRepository, never()).save(user); // No save operation
        assertEquals(1, user.getSavedPosts().size());
    }

    @Test
    public void testSavePostPostNotFound() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(false);

        // Act & Assert
        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.savePost(userEmail, postId)
        );

        assertEquals("Post with ID post123 not found", exception.getMessage());
        verify(userRepository, never()).save(user); // No save operation
    }

    @Test
    public void testVoteSuccess() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "post123";
        int pollIndex = 0;

        Post post = new Post();
        post.setId(postId);

        Poll poll = new Poll();
        poll.setVoters(new ArrayList<>());

        post.setPolls(List.of(poll));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.vote(userEmail, postId, pollIndex);

        // Assert
        assertTrue(post.getPolls().get(pollIndex).getVoters().contains(userEmail));
        verify(postRepository, times(1)).save(post); // Ensure the post is saved
    }

    @Test
    public void testVoteDuplicate() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "post123";
        int pollIndex = 0;

        Post post = new Post();
        post.setId(postId);

        Poll poll = new Poll();
        poll.setVoters(new ArrayList<>(List.of(userEmail))); // User has already voted

        post.setPolls(List.of(poll));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.vote(userEmail, postId, pollIndex);

        // Assert
        assertEquals(1, post.getPolls().get(pollIndex).getVoters().size());
        verify(postRepository, never()).save(post); // No save operation
    }

    @Test
    public void testVotePostNotFound() {
        // Arrange
        String userEmail = "user@example.com";
        String postId = "invalidPostId";
        int pollIndex = 0;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.vote(userEmail, postId, pollIndex)
        );

        assertEquals("Post with ID invalidPostId not found", exception.getMessage());
        verify(postRepository, never()).save(any(Post.class)); // No save operation
    }










}