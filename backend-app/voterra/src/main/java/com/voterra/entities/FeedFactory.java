package com.voterra.entities;
import org.springframework.data.annotation.Id;


import java.util.List;

public class FeedFactory {

    @Id
    private String postID;
    private String userID;
    private String postContent;
    private category category;
    private privacy privacy;
    private List<Poll> polls;

    public enum category {
        SPORTS, TECHNOLOGY, ENTERTAINMENT, HEALTH, EDUCATION, BUSINESS, FASHION, FOOD, JOBS, MEDICAL, CARS, EVENTS,
        PLACES, CELEBRITIES, NATURE, MOVIES, OTHER
    }
    public enum privacy {
        PUBLIC, PRIVATE
    }

    public FeedFactory(String postID, String userID, String postContent, category category, privacy privacy,
                       List<Poll> polls) {
        this.postID = postID;
        this.userID = userID;
        this.postContent = postContent;
        this.category = category;
        this.privacy = privacy;
        this.polls = polls;
    }

    public String getPostID() {
        return postID;
    }

    public String getUserID() {
        return userID;
    }

    public String getPostContent() {
        return postContent;
    }

    public FeedFactory.category getCategory() {
        return category;
    }

    public FeedFactory.privacy getPrivacy() {
        return privacy;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setCategory(FeedFactory.category category) {
        this.category = category;
    }

    public void setPrivacy(FeedFactory.privacy privacy) {
        this.privacy = privacy;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }
}
