package com.voterra.entities;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public class FeedFactory {
    private String postID;
    private String userID;
    private String postContent;
    private category category;
    private privacy privacy;
    private Date publishDate;
    private List<Poll> polls;

    public enum category {
        SPORTS, TECHNOLOGY, ENTERTAINMENT, HEALTH, EDUCATION, BUSINESS, FASHION, FOOD, JOBS, MEDICAL, CARS, EVENTS,
        PLACES, CELEBRITIES, NATURE, MOVIES, OTHER
    }
    public enum privacy {
        PUBLIC, PRIVATE
    }

    public FeedFactory(String postID, String userID, String postContent, category category, privacy privacy, Date publishDate, List<Poll> polls) {
        this.postID = postID;
        this.userID = userID;
        this.postContent = postContent;
        this.category = category;
        this.privacy = privacy;
        this.publishDate = publishDate;
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

    public Date getPublishDate() {
        return publishDate;
    }

    public List<Poll> getPolls() {
        return polls;
    }


}
