package com.voterra.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document (collection = "reportedPosts")
public class ReportedPost {
    private String postId;
    private String reporterId;
    private String reason;
    @Indexed
    private Date reportedDate ;

    public ReportedPost(String postId, String reporterId, String reason, Date reportedDate) {
        this.postId = postId;
        this.reporterId = reporterId;
        this.reason = reason;
        this.reportedDate = reportedDate;
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

    public Date getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
        this.reportedDate = reportedDate;
    }
}
