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
        String userEmail = "test@example.com";
        String userName = "testuser";
        String postContent = "This is a test post";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = new ArrayList<>();

        // Act
        feedFactory.setUserEmail(userEmail);
        feedFactory.setUserName(userName);
        feedFactory.setPostContent(postContent);
        feedFactory.setCategory(category);
        feedFactory.setPrivacy(privacy);
        feedFactory.setPolls(polls);

        // Assert
        assertEquals(userEmail, feedFactory.getUserEmail());
        assertEquals(userName, feedFactory.getUserName());
        assertEquals(postContent, feedFactory.getPostContent());
        assertEquals(category, feedFactory.getCategory());
        assertEquals(privacy, feedFactory.getPrivacy());
        assertEquals(polls, feedFactory.getPolls());
    }
}