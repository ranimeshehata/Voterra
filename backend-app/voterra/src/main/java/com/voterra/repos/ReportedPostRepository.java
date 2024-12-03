package com.voterra.repos;

import com.voterra.entities.ReportedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedPostRepository extends MongoRepository<ReportedPost, String> {
}
