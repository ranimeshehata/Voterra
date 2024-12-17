package com.voterra.Entities;

import com.voterra.entities.FeedFactory;
import com.voterra.entities.Poll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeedFactoryTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        FeedFactory feedFactory = new FeedFactory();
        String id = "kanyewest" ;
        String userEmail = "test@example.com";
        String username="7ngl";
        String postContent = "This is a test post";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = new ArrayList<>();

        // Act
        feedFactory.setUserEmail(userEmail);
        feedFactory.setUserName(username);
        feedFactory.setPostContent(postContent);
        feedFactory.setCategory(category);
        feedFactory.setPrivacy(privacy);
        feedFactory.setPolls(polls);
        feedFactory.setId(id);
        // Assert
        assertEquals(userEmail, feedFactory.getUserEmail());
        assertEquals(username,feedFactory.getUserName());
        assertEquals(postContent, feedFactory.getPostContent());
        assertEquals(category, feedFactory.getCategory());
        assertEquals(privacy, feedFactory.getPrivacy());
        assertEquals(polls, feedFactory.getPolls());
        assertEquals(id, feedFactory.getId());
    }
    @Test
    void testConstructor() {
        // Arrange
        String userEmail = "test@example.com";
        String username="leao";
        String postContent = "This is a test post";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = new ArrayList<>();

        // Act
        FeedFactory feedFactory = new FeedFactory(userEmail,username, postContent, category, privacy, polls);

        // Assert
        assertEquals(userEmail, feedFactory.getUserEmail());
        assertEquals(username,feedFactory.getUserName());
        assertEquals(postContent, feedFactory.getPostContent());
        assertEquals(category, feedFactory.getCategory());
        assertEquals(privacy, feedFactory.getPrivacy());
        assertEquals(polls, feedFactory.getPolls());
    }
}