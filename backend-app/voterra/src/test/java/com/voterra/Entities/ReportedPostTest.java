package com.voterra.Entities;

import com.voterra.entities.ReportedPost;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportedPostTest {

    @Test
    void testReportedPostConstructor() {
        // Arrange
        String postId = "12345";
        String reporterId = "67890";
        String reason = "Inappropriate content";
        Date reportedDate=new Date();
        // Act
        ReportedPost reportedPost = new ReportedPost(postId, reporterId, reason,reportedDate);

        // Assert
        assertNotNull(reportedPost);  // Ensure the reportedPost object is not null
        assertEquals(postId, reportedPost.getPostId());  // Verify the postId
        assertEquals(reporterId, reportedPost.getReporterId());  // Verify the reporterId
        assertEquals(reason, reportedPost.getReason());  // Verify the reason
    }

    @Test
    void testReportedPostSettersAndGetters() {
        // Arrange
        ReportedPost reportedPost = new ReportedPost(null, null, null,null);
        String postId = "12345";
        String reporterId = "67890";
        String reason = "Inappropriate content";

        // Act
        reportedPost.setPostId(postId);
        reportedPost.setReporterId(reporterId);
        reportedPost.setReason(reason);

        // Assert
        assertEquals(postId, reportedPost.getPostId());  // Verify the postId
        assertEquals(reporterId, reportedPost.getReporterId());  // Verify the reporterId
        assertEquals(reason, reportedPost.getReason());  // Verify the reason
    }
}

