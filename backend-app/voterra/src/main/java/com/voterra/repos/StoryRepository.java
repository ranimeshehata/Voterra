package com.voterra.repos;

import com.voterra.entities.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
}
