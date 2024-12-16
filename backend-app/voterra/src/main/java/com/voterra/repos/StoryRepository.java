package com.voterra.repos;

import com.voterra.entities.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
    List<Story> findByUserEmailOrderByPublishedDateAsc(String userEmail);

    @Query("{ 'userEmail': { $in: ?0 }, 'privacy': { $in: ['FRIENDS', 'PUBLIC'] } }")
    List<Story> findFriendStoriesOrderByPublishedDateAsc(List<String> friendEmails);

    @Query("{ 'userEmail': ?0, 'privacy': 'PUBLIC' }")
    List<Story> findNonFriendStoriesOrderByPublishedDateAsc(String userEmail);
}
