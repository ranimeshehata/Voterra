package com.voterra.entities;

import java.util.Date;
import java.util.List;

public class Post extends FeedFactory{
    public Post(String postID, String userID, String postContent, FeedFactory.category category,
                FeedFactory.privacy privacy, Date publishDate, List<Poll> polls) {
        super(postID, userID, postContent, category, privacy, publishDate, polls);
    }
}
