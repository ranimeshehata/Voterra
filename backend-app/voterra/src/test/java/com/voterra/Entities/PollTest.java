package com.voterra.Entities;

import com.voterra.entities.Poll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PollTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Poll poll = new Poll();
        String question = "What is your favorite color?";
        List<String> voters = List.of("voter1", "voter2");
        // Act
        poll.setPollContent(question);
        poll.setVoters(voters);

        // Assert
        assertEquals(question, poll.getPollContent());
        assertEquals(voters, poll.getVoters());
    }
    @Test
    void testPollConstructor() {
        // Arrange
        String pollContent = "What is your favorite programming language?";
        List<String> voters = new ArrayList<>();
        voters.add("voter1@example.com");
        voters.add("voter2@example.com");

        // Act
        Poll poll = new Poll(pollContent, voters);

        // Assert
        assertNotNull(poll);  // Ensure the poll object is not null
        assertEquals(pollContent, poll.getPollContent());  // Verify the poll content
        assertEquals(voters, poll.getVoters());  // Verify the voters list
    }
}