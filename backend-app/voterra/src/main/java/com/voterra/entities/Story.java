package com.voterra.entities;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "stories")
public class Story extends FeedFactory{
    @Indexed(expireAfterSeconds = 86400)
    private Date publishedDate;
    public Story(String postID, String userID, String postContent, FeedFactory.category category,
                FeedFactory.privacy privacy, List<Poll> polls, Date publishedDate) {
        super(postID, userID, postContent, category, privacy, polls);
        this.publishedDate = publishedDate;
    }
    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
