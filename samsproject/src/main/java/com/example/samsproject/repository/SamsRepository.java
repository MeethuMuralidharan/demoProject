package com.example.samsproject.repository;

import com.example.samsproject.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SamsRepository extends MongoRepository<Customer,String> {
    Customer findByUserId(String id);
    ArrayList<Customer> findByParentId(String id);
}
