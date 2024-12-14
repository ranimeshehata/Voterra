package com.voterra.entities;


import java.util.List;

public class FeedFactory {

    private String userEmail;
    private String userName;
    private String postContent;
    private category category;
    private privacy privacy;
    private List<Poll> polls;

    public enum category {
        SPORTS, TECHNOLOGY, ENTERTAINMENT, HEALTH, EDUCATION, BUSINESS, FASHION, FOOD, JOBS, MEDICAL, CARS, EVENTS,
        PLACES, CELEBRITIES, NATURE, MOVIES, OTHER
    }
    public enum privacy {
        PUBLIC,FRIENDS, PRIVATE
    }

    public FeedFactory(String userEmail,String userName, String postContent, category category, privacy privacy,
                       List<Poll> polls) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.postContent = postContent;
        this.category = category;
        this.privacy = privacy;
        this.polls = polls;
    }

    public FeedFactory() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {return userName;}

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

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) { this.userName = userName;}

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
