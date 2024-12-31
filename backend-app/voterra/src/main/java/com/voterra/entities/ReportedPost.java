package com.voterra.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document (collection = "reportedPosts")
public class ReportedPost {
    @Id
    private String postId;
    private List<String> reportersId;


    public ReportedPost(String postId, List<String> reportersId) {
        this.postId = postId;
        this.reportersId = reportersId;
    }

    public ReportedPost() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<String> getReportersId() {
        return reportersId;
    }

    public void setReportersId(List<String> reportersId) {
        this.reportersId = reportersId;
    }

}
