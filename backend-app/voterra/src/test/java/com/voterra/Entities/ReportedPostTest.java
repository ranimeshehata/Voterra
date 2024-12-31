package com.voterra.Entities;

import com.voterra.entities.ReportedPost;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportedPostTest {

    @Test
    void testReportedPostConstructor() {
        // Arrange
        String postId = "12345";
        List<String> reportersId = List.of("67890");
        String reason = "Inappropriate content";
        Date reportedDate=new Date();
        // Act
        ReportedPost reportedPost = new ReportedPost(postId, reportersId);

        // Assert
        assertNotNull(reportedPost);  // Ensure the reportedPost object is not null
        assertEquals(postId, reportedPost.getPostId());  // Verify the postId
        assertEquals(reportersId, reportedPost.getReportersId());  // Verify the reporterId

    }

    @Test
    void testReportedPostSettersAndGetters() {
        // Arrange
        ReportedPost reportedPost = new ReportedPost(null, null);
        String postId = "12345";
        List<String> reporterId = List.of("67890");
        String reason = "Inappropriate content";

        // Act
        reportedPost.setPostId(postId);
        reportedPost.setReportersId(reporterId);

        // Assert
        assertEquals(postId, reportedPost.getPostId());  // Verify the postId
        assertEquals(reporterId, reportedPost.getReportersId());  // Verify the reporterId
    }
}

