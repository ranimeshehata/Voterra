package com.voterra.DTOs;

import com.voterra.entities.Post;

public class ReportedPostDTO {
    private Post post;
    private int numberOfReports;

    public ReportedPostDTO(Post post, int numberOfReports) {
        this.post = post;
        this.numberOfReports = numberOfReports;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
