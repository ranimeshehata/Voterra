package com.voterra.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "reportedPosts")
public class ReportedPost {
    private String postId;
    private String reporterId;
    private String reason;

    public ReportedPost(String postId, String reporterId, String reason) {
        this.postId = postId;
        this.reporterId = reporterId;
        this.reason = reason;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
