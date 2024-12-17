package com.voterra.Entities;

import com.voterra.entities.FeedFactory;
import com.voterra.entities.Poll;
import com.voterra.entities.Story;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoryTest {

    @Test
    void testStoryConstructorAndGetters() {
        // Arrange: Prepare the necessary data for the test
        String userEmail = "test@example.com";
        String username="arabElSwerky";
        String postContent = "This is a test story";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = Arrays.asList(new Poll("Poll 1", Arrays.asList("user1", "user2")));
        Date publishedDate = new Date();

        // Act: Create the Story object using the constructor
        Story story = new Story(userEmail,username ,postContent, category, privacy, polls, publishedDate);

        // Assert: Verify that the values set in the constructor are correctly stored in the object
        assertEquals(userEmail, story.getUserEmail());
        assertEquals(username,story.getUserName());
        assertEquals(postContent, story.getPostContent());
        assertEquals(category, story.getCategory());
        assertEquals(privacy, story.getPrivacy());
        assertEquals(polls, story.getPolls());
        assertEquals(publishedDate, story.getPublishedDate());
    }

    @Test
    void testSetPublishedDate() {
        // Arrange: Prepare the Story object with initial values
        String userEmail = "test@example.com";
        String username="zindeneZidane";
        String postContent = "Test story content";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = Arrays.asList(new Poll("Poll 1", Arrays.asList("user1", "user2")));
        Date publishedDate = new Date();
        Story story = new Story(userEmail,username, postContent, category, privacy, polls, publishedDate);

        // Act: Change the published date
        Date newPublishedDate = new Date(System.currentTimeMillis() + 1000000000L); // New date
        story.setPublishedDate(newPublishedDate);

        // Assert: Verify that the published date was updated correctly
        assertEquals(newPublishedDate, story.getPublishedDate());
    }
}
