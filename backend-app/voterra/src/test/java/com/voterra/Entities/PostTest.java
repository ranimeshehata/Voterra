package com.voterra.Entities;

import com.voterra.entities.FeedFactory;
import com.voterra.entities.Poll;
import com.voterra.entities.Post;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostTest {

    @Test
    void testPostConstructor() {
        // Arrange
        String userEmail = "test@example.com";
        String username="KevinDeBruyne";
        String postContent = "This is a test post";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = new ArrayList<>();
        Date publishedDate = new Date();

        // Act
        Post post = new Post(userEmail, username,postContent, category, privacy, polls, publishedDate);

        // Assert
        assertNotNull(post);  // Ensure the post object is not null
        assertEquals(userEmail, post.getUserEmail());  // Verify the userEmail
        assertEquals(username,post.getUserName());
        assertEquals(postContent, post.getPostContent());  // Verify the postContent
        assertEquals(category, post.getCategory());  // Verify the category
        assertEquals(privacy, post.getPrivacy());  // Verify the privacy
        assertEquals(polls, post.getPolls());  // Verify the polls
        assertEquals(publishedDate, post.getPublishedDate());  // Verify the publishedDate
    }

    @Test
    void testPostSettersAndGetters() {
        // Arrange
        Post post = new Post();
        String userEmail = "test@example.com";
        String username="erlingHaaland";
        String postContent = "This is a test post";
        FeedFactory.category category = FeedFactory.category.OTHER;
        FeedFactory.privacy privacy = FeedFactory.privacy.PUBLIC;
        List<Poll> polls = new ArrayList<>();
        Date publishedDate = new Date();

        // Act
        post.setUserEmail(userEmail);
        post.setUserName(username);
        post.setPostContent(postContent);
        post.setCategory(category);
        post.setPrivacy(privacy);
        post.setPolls(polls);
        post.setPublishedDate(publishedDate);

        // Assert
        assertEquals(userEmail, post.getUserEmail());  // Verify the userEmail
        assertEquals(username,post.getUserName());
        assertEquals(postContent, post.getPostContent());  // Verify the postContent
        assertEquals(category, post.getCategory());  // Verify the category
        assertEquals(privacy, post.getPrivacy());  // Verify the privacy
        assertEquals(polls, post.getPolls());  // Verify the polls
        assertEquals(publishedDate, post.getPublishedDate());  // Verify the publishedDate
    }
}
