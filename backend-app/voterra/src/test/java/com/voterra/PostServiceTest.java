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
import org.springframework.data.domain.Sort;
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
    void testGetSavedPosts() {
        String email = "testuser@example.com";
        int page = 0;
        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedDate"));

        User user = new User();
        user.setEmail(email);
        user.setSavedPosts(List.of("post1", "post2"));

        Post post1 = new Post("user1@example.com","user1" ,"Post 1", null,
                Post.privacy.PUBLIC, null, new Date());
        post1.setId("post1");
        Post post2 = new Post("user2@example.com", "user2","Post 2", null,
                Post.privacy.PUBLIC, null, new Date());
        post2.setId("post2");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(postRepository.findByIdIn(user.getSavedPosts(), pageable)).thenReturn(List.of(post1, post2));

        List<Post> result = postService.getSavedPosts(email, page);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(post1));
        assertTrue(result.contains(post2));
    }

    @Test
    public void testSavePostSuccess() {
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);
        user.setSavedPosts(new ArrayList<>());

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(true);

        postService.savePost(userEmail, postId);

        verify(userRepository, times(1)).save(user);
        assert user.getSavedPosts().contains(postId);
    }

    @Test
    public void testSavePostAlreadySaved() {
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);
        user.setSavedPosts(new ArrayList<>());
        user.getSavedPosts().add(postId);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(true);

        postService.savePost(userEmail, postId);

        verify(userRepository, never()).save(user);
        assertEquals(1, user.getSavedPosts().size());
    }

    @Test
    public void testSavePostPostNotFound() {
        String userEmail = "user@example.com";
        String postId = "post123";

        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(postRepository.existsById(postId)).thenReturn(false);

        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.savePost(userEmail, postId)
        );

        assertEquals("Post with ID post123 not found", exception.getMessage());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void testVoteSuccess() {
        String userEmail = "user@example.com";
        String postId = "post123";
        int pollIndex = 0;

        Post post = new Post();
        post.setId(postId);

        Poll poll = new Poll();
        poll.setVoters(new ArrayList<>());

        post.setPolls(List.of(poll));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.vote(userEmail, postId, pollIndex);

        assertTrue(post.getPolls().get(pollIndex).getVoters().contains(userEmail));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testVoteDuplicate() {
        String userEmail = "user@example.com";
        String postId = "post123";
        int pollIndex = 0;

        Post post = new Post();
        post.setId(postId);

        Poll poll = new Poll();
        poll.setVoters(new ArrayList<>(List.of(userEmail))); 

        post.setPolls(List.of(poll));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.vote(userEmail, postId, pollIndex);

        assertEquals(1, post.getPolls().get(pollIndex).getVoters().size());
        verify(postRepository, never()).save(post);
    }

    @Test
    public void testVotePostNotFound() {
        String userEmail = "user@example.com";
        String postId = "invalidPostId";
        int pollIndex = 0;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.vote(userEmail, postId, pollIndex)
        );

        assertEquals("Post with ID invalidPostId not found", exception.getMessage());
        verify(postRepository, never()).save(any(Post.class));
    }


    @Test
    void testGetUserPosts_UserAccessingOwnPosts() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);

        Post userPost = new Post("testuser@example.com", "testuser" ,"User Post",
                null, Post.privacy.PUBLIC, null, new Date());

        when(postRepository.findByUserEmail(eq(currentUserEmail), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(userPost));

        List<Post> result = postService.getUserPosts(currentUserEmail, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(userPost));
    }

    @Test
    void testGetUserPosts_UserAccessingFriendPosts() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);

        String friendEmail = "friend@example.com";
        Post friendPost = new Post(friendEmail, "testuser" ,"Friend Post", null,
                Post.privacy.FRIENDS, null, new Date());

        when(userService.getFriends(currentUserEmail)).thenReturn(List.of(friendEmail));
        when(postRepository.findByUserEmailInWithSpecificPrivacy(eq(List.of(friendEmail)), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(friendPost));

        List<Post> result = postService.getUserPosts(friendEmail, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(friendPost));
    }

    @Test
    void testGetUserPosts_UserAccessingPublicPosts() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);

        String publicUserEmail = "publicuser@example.com";
        Post publicPost = new Post(publicUserEmail, "testuser" ,"Public Post", null, Post.privacy.PUBLIC, null, new Date());

        when(userService.getFriends(currentUserEmail)).thenReturn(Collections.singletonList("friend@example.com"));
        when(postRepository.findByUserEmailWithPublicPrivacy(eq(publicUserEmail), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(publicPost));

        List<Post> result = postService.getUserPosts(publicUserEmail, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(publicPost));
    }
    @Test
    public void testCreatePost() {
        Post post = new Post();
        post.setUserEmail("test@example.com");
        post.setPostContent("Test Content");
        when(postRepository.save(post)).thenReturn(post);

        Post createdPost = postService.createPost(post);

        assertNotNull(createdPost);
        assertEquals("test@example.com", createdPost.getUserEmail());
        assertEquals("Test Content", createdPost.getPostContent());

        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testDeletePostById() {
        String postId = "123";

        when(postRepository.existsById(postId)).thenReturn(true);

        postService.deletePostById(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    public void testDeletePostById_NotFound() {
        String postId = "123";

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(PostNotFoundException.class, () -> {
            postService.deletePostById(postId);
        });

        verify(postRepository, times(1)).existsById(postId);
        verify(postRepository, times(0)).deleteById(postId);
    }
}