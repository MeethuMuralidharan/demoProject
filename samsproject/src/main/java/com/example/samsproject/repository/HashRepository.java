package com.example.samsproject.repository;

import com.example.samsproject.model.Customer;
import com.example.samsproject.model.HashData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends MongoRepository<HashData,String> {
    public HashData findByParentId(String id);
}
