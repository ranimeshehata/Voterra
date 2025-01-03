package com.voterra;
import com.voterra.DTOs.ReportedPostDTO;
import com.voterra.entities.*;
import com.voterra.entities.FeedFactory;
import com.voterra.entities.Poll;
import com.voterra.entities.Post;
import com.voterra.entities.User;
import com.voterra.exceptions.PostNotFoundException;
import com.voterra.repos.PostRepository;
import com.voterra.repos.ReportedPostRepository;
import com.voterra.repos.UserRepository;
import com.voterra.services.PostService;
import com.voterra.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
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

    @Mock
    private ReportedPostRepository reportedPostRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetPaginatedPosts() {
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        String currentUserEmail = "testuser@example.com";
//        when(authentication.getName()).thenReturn(currentUserEmail);
//
//        Post userPost = new Post("testuser@example.com","testuser" ,"User Post", FeedFactory.category.OTHER, Post.privacy.PUBLIC, null, new Date());
//        Post friendPost = new Post("friend@example.com", "testuser" ,"Friend Post", FeedFactory.category.OTHER, Post.privacy.FRIENDS, null, new Date());
//        Post nonFriendPost = new Post("nonfriend@example.com", "testuser" ,"Non-Friend Post", FeedFactory.category.OTHER, Post.privacy.PUBLIC, null, new Date());
//
//        when(postRepository.findByUserEmail(eq(currentUserEmail), any(PageRequest.class)))
//                .thenReturn(Collections.singletonList(userPost));
//
//        when(userService.getFriends(currentUserEmail))
//                .thenReturn(Collections.singletonList("friend@example.com"));
//
//        when(postRepository.findByUserEmailInWithSpecificPrivacy(eq(List.of("friend@example.com")), any(PageRequest.class)))
//                .thenReturn(Collections.singletonList(friendPost));
//
//        when(postRepository.findByUserEmailNotInWithPublicPrivacy(eq(Arrays.asList("friend@example.com", "testuser@example.com")), any(PageRequest.class)))
//                .thenReturn(Collections.singletonList(nonFriendPost));
//
//        List<Post> result = postService.getPaginatedPosts("all", 0);
//
//        assertNotNull(result);
//        assertEquals(3, result.size());
//        assertTrue(result.contains(userPost));
//        assertTrue(result.contains(friendPost));
//        assertTrue(result.contains(nonFriendPost));
//    }

    @Test
    void testGetPaginatedPosts_AllCategory() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "test@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);


        String email = "test@example.com";
        int page = 0;
        String category = "all";
        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedDate"));

        List<Post> userPosts = List.of(
                new Post(email, email, "User Name", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date()),
                new Post(email, email, "User Name", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date())
        );
        when(postRepository.findByUserEmail(email, pageable)).thenReturn(userPosts);

        List<String> friends = List.of("friend1@example.com", "friend2@example.com");
        when(userService.getFriends(email)).thenReturn(friends);

        List<Post> friendPosts = List.of(
                new Post("friend1@example.com", "friend1@example.com", "Friend One", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date()),
                new Post("friend2@example.com", "friend2@example.com", "Friend Two", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date())
        );
        when(postRepository.findByUserEmailInWithSpecificPrivacy(friends, pageable)).thenReturn(friendPosts);

        List<Post> result = postService.getPaginatedPosts(category, page);

        List<Post> expected = new ArrayList<>();
        expected.addAll(userPosts);
        expected.addAll(friendPosts);
        expected.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));

        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    void testGetPaginatedPosts_SpecificCategory() {
        // Setup SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "test@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);

        String email = "test@example.com";
        int page = 0;
        FeedFactory.category category = FeedFactory.category.OTHER;
        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedDate"));

        List<Post> userPosts = List.of(
                new Post(email, email, "User Post 1", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date(1670000000000L)),
                new Post(email, email, "User Post 2", FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date(1670000005000L))
        );
        when(postRepository.findByUserEmailAndCategory(email, String.valueOf(FeedFactory.category.OTHER), pageable)).thenReturn(userPosts);

        List<String> friends = List.of("friend1@example.com", "friend2@example.com");
        when(userService.getFriends(email)).thenReturn(friends);

        List<Post> friendPosts = List.of(
                new Post("friend1@example.com", "friend1@example.com", "Friend Post 1", FeedFactory.category.OTHER, FeedFactory.privacy.FRIENDS, null, new Date(1670000010000L)),
                new Post("friend2@example.com", "friend2@example.com", "Friend Post 2", FeedFactory.category.OTHER, FeedFactory.privacy.FRIENDS, null, new Date(1670000015000L))
        );
        when(postRepository.findByUserEmailInWithSpecificPrivacyAndCategory(friends, String.valueOf(FeedFactory.category.OTHER), pageable))
                .thenReturn(friendPosts);

        List<Post> result = postService.getPaginatedPosts(String.valueOf(category), page);


        List<Post> expected = new ArrayList<>();
        expected.addAll(userPosts);
        expected.addAll(friendPosts);
        expected.sort((post1, post2) -> post2.getPublishedDate().compareTo(post1.getPublishedDate()));

        assertEquals(expected.size(), result.size(), "Post count mismatch");
        assertEquals(expected, result, "Posts do not match");
    }

//    @Test
//    void testGetSavedPosts() {
//        String email = "testuser@example.com";
//        int page = 0;
//        PageRequest pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "publishedDate"));
//
//        User user = new User();
//        user.setEmail(email);
//        user.setSavedPosts(List.of("post1", "post2"));
//
//        Post post1 = new Post("user1@example.com","user1" ,"Post 1", null,
//                Post.privacy.PUBLIC, null, new Date());
//        post1.setId("post1");
//        Post post2 = new Post("user2@example.com", "user2","Post 2", null,
//                Post.privacy.PUBLIC, null, new Date());
//        post2.setId("post2");
//
//        when(userRepository.findByEmail(email)).thenReturn(user);
//        when(postRepository.findByIdIn(user.getSavedPosts(), pageable)).thenReturn(List.of(post1, post2));
//
//        List<Post> result = postService.getSavedPosts(email, page);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertTrue(result.contains(post1));
//        assertTrue(result.contains(post2));
//    }

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

    @Test
    void testReportPost_NewPost() {
        ReportedPost newReportedPost = new ReportedPost("postId", new LinkedList<>(List.of("user1@example.com")));
        when(reportedPostRepository.findById("postId")).thenReturn(Optional.empty());

        User user = new User();
        user.setReportedPosts(new ArrayList<>());
        when(userRepository.findByEmail("user1@example.com")).thenReturn(user);

        String result = postService.reportPost(newReportedPost);

        assertEquals("Post reported successfully", result);
        verify(reportedPostRepository).save(newReportedPost);
        verify(userRepository).save(user);
        assertTrue(user.getReportedPosts().contains("postId"));
    }

    @Test
    void testReportPost_AlreadyReportedBySameUser() {
        ReportedPost existingReportedPost = new ReportedPost("postId", new LinkedList<>(List.of("user1@example.com")));
        when(reportedPostRepository.findById("postId")).thenReturn(Optional.of(existingReportedPost));

        ReportedPost newReportAttempt = new ReportedPost("postId", new LinkedList<>(List.of("user1@example.com")));

        String result = postService.reportPost(newReportAttempt);

        assertEquals("You have already reported this post", result);
        verify(reportedPostRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testReportPost_NewUserReportsAlreadyReportedPost() {
        String postId = "postId1";
        String newUserEmail = "newUser@example.com";
        String existingUserEmail = "existingUser@example.com";

        ReportedPost existingReportedPost = new ReportedPost(postId, new LinkedList<>(List.of(existingUserEmail)));

        ReportedPost newReportedPost = new ReportedPost(postId, new LinkedList<>(List.of(newUserEmail)));

        when(reportedPostRepository.findById(postId)).thenReturn(Optional.of(existingReportedPost));

        User newUser = new User();
        newUser.setEmail(newUserEmail);
        newUser.setReportedPosts(new LinkedList<>());
        when(userRepository.findByEmail(newUserEmail)).thenReturn(newUser);

        String result = postService.reportPost(newReportedPost);

        assertEquals("Post reported successfully", result);
        verify(reportedPostRepository).save(existingReportedPost);
        verify(userRepository).save(newUser);
        assertTrue(existingReportedPost.getReportersId().contains(newUserEmail));
        assertTrue(newUser.getReportedPosts().contains(postId));
    }


    @Test
    void testGetReportedPosts() {
        ReportedPost reportedPost1 = new ReportedPost("postId1", new LinkedList<>(List.of("user1@example.com")));
        ReportedPost reportedPost2 = new ReportedPost("postId2", new LinkedList<>(List.of("user2@example.com", "user3@example.com")));

        Post post1 = new Post("user1@example.com", "User1", "Content1", FeedFactory.category.BUSINESS,
                FeedFactory.privacy.PUBLIC, null, new Date());
        Post post2 = new Post("user2@example.com", "User2", "Content2", FeedFactory.category.SPORTS,
                FeedFactory.privacy.PUBLIC, null, new Date());

        List<ReportedPost> reportedPosts = List.of(reportedPost1, reportedPost2);

        when(reportedPostRepository.findAll(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(reportedPosts));
        when(postRepository.findById("postId1")).thenReturn(Optional.of(post1));
        when(postRepository.findById("postId2")).thenReturn(Optional.of(post2));

        List<ReportedPostDTO> result = postService.getReportedPosts(0);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getNumberOfReports());
        assertEquals(2, result.get(1).getNumberOfReports());
        assertEquals("Content1", result.get(0).getPost().getPostContent());
        assertEquals("Content2", result.get(1).getPost().getPostContent());
    }


    @Test
    void testDeleteReportedPost() {
        ReportedPost reportedPost = new ReportedPost("postId", new LinkedList<>(List.of("user1@example.com")));
        User user = new User();
        user.setSavedPosts(new ArrayList<>(List.of("postId")));
        user.setReportedPosts(new ArrayList<>(List.of("postId")));
        List<User> users = List.of(user);

        when(reportedPostRepository.findById("postId")).thenReturn(Optional.of(reportedPost));
        when(userRepository.findAll()).thenReturn(users);

        postService.deleteReportedPost("postId");

        assertFalse(user.getSavedPosts().contains("postId"));
        assertFalse(user.getReportedPosts().contains("postId"));
        verify(postRepository).deleteById("postId");
        verify(reportedPostRepository).deleteById("postId");
    }

    @Test
    void testLeaveReportedPost() {
        ReportedPost reportedPost = new ReportedPost("postId", new LinkedList<>(List.of("user1@example.com")));
        User user = new User();
        user.setReportedPosts(new ArrayList<>(List.of("postId")));
        List<User> users = List.of(user);

        when(reportedPostRepository.findById("postId")).thenReturn(Optional.of(reportedPost));
        when(userRepository.findAll()).thenReturn(users);

        postService.leaveReportedPost("postId");

        assertFalse(user.getReportedPosts().contains("postId"));
        verify(reportedPostRepository).deleteById("postId");
        verify(userRepository).save(user);
    }


    @Test
    void testSearchPosts() {
        // Arrange
        String searchContent = "example";
        int page = 0;
        int size = 5;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedDate"));

        String currentUserEmail = "user@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUserEmail);

        List<String> friends = List.of("friend1@example.com", "friend2@example.com");
        when(userService.getFriends(currentUserEmail)).thenReturn(friends);

        List<Post> matchedPosts = List.of(
                new Post( "user@example.com", "User One", "This is an example post.",
                        FeedFactory.category.OTHER, FeedFactory.privacy.PRIVATE, null, new Date()),
                new Post("friend1@example.com", "Friend One", "Another example post.",
                        FeedFactory.category.OTHER, FeedFactory.privacy.FRIENDS, null, new Date()),
                new Post("public@example.com", "Public User", "Public example post.",
                        FeedFactory.category.OTHER, FeedFactory.privacy.PUBLIC, null, new Date()),
                new Post("other@example.com", "Other User", "Another private post.",
                        FeedFactory.category.OTHER, FeedFactory.privacy.PRIVATE, null, new Date())
        );
        when(postRepository.findByPostContentContaining(searchContent, pageable)).thenReturn(matchedPosts);

        // Act
        List<Post> result = postService.searchPosts(searchContent, page);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Only 3 posts should be returned (PUBLIC, FRIENDS with matching user, and the user's own post)
        assertTrue(result.stream().anyMatch(post -> post.getUserEmail().equals("user@example.com")));
        assertTrue(result.stream().anyMatch(post -> post.getUserEmail().equals("friend1@example.com")));
        assertTrue(result.stream().anyMatch(post -> post.getUserEmail().equals("public@example.com")));
    }
}

