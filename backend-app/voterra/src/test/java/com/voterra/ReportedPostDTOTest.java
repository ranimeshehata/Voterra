package com.voterra.DTOs;

import com.voterra.entities.Post;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportedPostDTOTest {

    @Test
    public void testConstructorAndGetters() {
        Post post = new Post();
        int numberOfReports = 5;

        ReportedPostDTO reportedPostDTO = new ReportedPostDTO(post, numberOfReports);

        assertNotNull(reportedPostDTO);
        assertEquals(post, reportedPostDTO.getPost());
        assertEquals(numberOfReports, reportedPostDTO.getNumberOfReports());
    }

    @Test
    public void testSetPost() {
        Post post = new Post();
        ReportedPostDTO reportedPostDTO = new ReportedPostDTO(null, 0);

        reportedPostDTO.setPost(post);

        assertEquals(post, reportedPostDTO.getPost());
    }

    @Test
    public void testSetNumberOfReports() {
        int numberOfReports = 10;
        ReportedPostDTO reportedPostDTO = new ReportedPostDTO(null, 0);

        reportedPostDTO.setNumberOfReports(numberOfReports);

        assertEquals(numberOfReports, reportedPostDTO.getNumberOfReports());
    }
}