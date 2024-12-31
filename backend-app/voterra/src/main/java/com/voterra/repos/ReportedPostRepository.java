package com.voterra.repos;

import com.voterra.entities.ReportedPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;


@Repository
public interface ReportedPostRepository extends MongoRepository<ReportedPost, String>,
        PagingAndSortingRepository<ReportedPost, String> {
}