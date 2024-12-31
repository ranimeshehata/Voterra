package com.voterra.repos;

import com.voterra.entities.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserEmail(String userEmail, Pageable pageable);

    // Fetch posts by friends with privacy 'PUBLIC' or 'FRIENDS'
    @Query("{ 'userEmail': { $in: ?0 }, 'privacy': { $in: ['PUBLIC', 'FRIENDS'] } }")
    List<Post> findByUserEmailInWithSpecificPrivacy(List<String> friends, Pageable pageable);

    // Fetch non-friend posts with privacy 'PUBLIC'
    @Query("{ 'userEmail': { $nin: ?0 }, 'privacy': 'PUBLIC' }")
    List<Post> findByUserEmailNotInWithPublicPrivacy(List<String> excludedEmails, Pageable pageable);

    // Fetch posts by post id in a saved post list
    List<Post> findByIdIn(List<String> savedPostIds, Pageable pageable);

    @Query("{ 'userEmail': ?0, 'privacy': 'PUBLIC' }")
    List<Post> findByUserEmailWithPublicPrivacy(String userEmail, Pageable pageable);

    // Fetch posts by user email and category
    List<Post> findByUserEmailAndCategory(String email, String category, PageRequest pageable);

    // Fetch posts by user email in a list of friends with privacy 'PUBLIC' or 'FRIENDS' and category
    @Query("{ 'userEmail': { $in: ?0 }, 'privacy': { $in: ['PUBLIC', 'FRIENDS'] }, 'category': ?1 }")
    List<Post> findByUserEmailInWithSpecificPrivacyAndCategory(List<String> friends, String category, PageRequest pageable);

    // Fetch non-friend posts with privacy 'PUBLIC' and category
    @Query("{ 'userEmail': { $nin: ?0 }, 'privacy': 'PUBLIC', 'category': ?1 }")
    List<Post> findByUserEmailNotInWithPublicPrivacyAndCategory(List<String> excludedEmails, String category, PageRequest pageable);

    // Fetch posts by post content
    @Query("{ 'postContent': { $regex: ?0, $options: 'i' } }")
    List<Post> findByPostContentContaining(String postContent, PageRequest pageable);
}

