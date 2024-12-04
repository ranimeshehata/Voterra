package com.voterra;

import com.voterra.entities.Poll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

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
}