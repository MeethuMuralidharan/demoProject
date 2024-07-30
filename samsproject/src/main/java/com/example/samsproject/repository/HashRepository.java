package com.example.samsproject.repository;

import com.example.samsproject.model.HashData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HashRepository extends MongoRepository<HashData,String> {
}
