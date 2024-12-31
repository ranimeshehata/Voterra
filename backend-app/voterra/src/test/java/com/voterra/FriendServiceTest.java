package com.voterra;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import com.voterra.services.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample user data
        user = new User();
        user.setEmail("test@example.com");
        user.setFriends(new ArrayList<>(Arrays.asList("friend1@example.com", "friend2@example.com")));
    }

    @Test
    void testGetFriends_UserNotFound() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.getFriends("test@example.com", 0));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testGetFriends_EmptyFriendsList() {
        user.setFriends(new ArrayList<>());
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        List<User> result = friendService.getFriends("test@example.com", 0);
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetFriends_PageOutOfBounds() {
        // Set up the user with a small friends list
        user.setFriends(new ArrayList<>(Arrays.asList("friend1@example.com", "friend2@example.com")));

        // Mock the repository to return this user
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        // Call getFriends with a page that goes out of bounds
        List<User> result = friendService.getFriends("test@example.com", 5); // 5 * PAGE_SIZE > friends list size

        // Assert that the result is an empty list
        assertTrue(result.isEmpty(), "Expected an empty list when page index exceeds friend list size.");
    }


    @Test
    void testGetFriends_PaginatedResults() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findAllById(anyList())).thenReturn(Arrays.asList(new User("friend1@example.com", null, null, null, null, null, null, null, null, null)));

        List<User> result = friendService.getFriends("test@example.com", 0);
        assertEquals(1, result.size());
        assertEquals("friend1@example.com", result.get(0).getEmail());
    }

    @Test
    void testAddFriend_UserNotFound() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.addFriend("test@example.com", "friend3@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testAddFriend_MaliciousUserAddingSelf() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.addFriend("test@example.com", "test@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }
    @Test
    void testAddFriend_InitializeFriendsList() {
        // Set up the user with a null friends list
        user.setFriends(null);

        // Mock the repository to return this user and the existence of the friend
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsById("friend1@example.com")).thenReturn(true);

        // Call addFriend
        friendService.addFriend("test@example.com", "friend1@example.com");

        // Verify that the friends list is initialized and the friend is added
        assertNotNull(user.getFriends(), "Friends list should be initialized.");
        assertTrue(user.getFriends().contains("friend1@example.com"), "Friend should be added to the initialized friends list.");
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testAddFriend_FriendNotExist() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsById("friend3@example.com")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.addFriend("test@example.com", "friend3@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testAddFriend_AlreadyAFriend() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsById("friend1@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.addFriend("test@example.com", "friend1@example.com"));
        assertEquals("User already a friend", exception.getMessage());
    }

    @Test
    void testAddFriend_Success() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsById("friend3@example.com")).thenReturn(true);

        friendService.addFriend("test@example.com", "friend3@example.com");
        verify(userRepository, times(1)).save(user);
        assertTrue(user.getFriends().contains("friend3@example.com"));
    }

    @Test
    void testRemoveFriend_UserNotFound() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.removeFriend("test@example.com", "friend1@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testRemoveFriend_MaliciousUserRemovingSelf() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.removeFriend("test@example.com", "test@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testRemoveFriend_NotInFriendsList() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.removeFriend("test@example.com", "friend3@example.com"));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testRemoveFriend_Success() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        friendService.removeFriend("test@example.com", "friend1@example.com");
        verify(userRepository, times(1)).save(user);
        assertFalse(user.getFriends().contains("friend1@example.com"));
    }

    @Test
    void testSuggestFriends_UserNotFound() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> friendService.suggestFriends("test@example.com", 0));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testSuggestFriends_NoSuggestions() {
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        List<User> result = friendService.suggestFriends("test@example.com", 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSuggestFriends_PaginatedResults() {
        User friend1 = new User("friend1@example.com", null, null, null, null, null, Arrays.asList("friend3@example.com"), null, null, null);
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findById("friend1@example.com")).thenReturn(Optional.of(friend1));
        when(userRepository.findAllById(anyList())).thenReturn(Arrays.asList(new User("friend3@example.com", null, null, null, null, null, null, null, null, null)));

        List<User> result = friendService.suggestFriends("test@example.com", 0);
        assertEquals(1, result.size());
        assertEquals("friend3@example.com", result.get(0).getEmail());
    }
}

