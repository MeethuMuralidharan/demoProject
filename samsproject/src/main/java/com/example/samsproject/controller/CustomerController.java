package com.example.samsproject.controller;

import com.example.samsproject.model.Customer;
import com.example.samsproject.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/samsproject")
public class CustomerController {
    @Autowired
    KafkaConsumerService kafkaConsumerService;
    @GetMapping("/getData")
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = kafkaConsumerService.findAllTasks();
        log.info("**********DBConnected**********" + customerList);
        return customerList;
    }
    @GetMapping("/{id}")
    public Customer getUsersById(@PathVariable String id) throws IOException {
        return kafkaConsumerService.getByUserId(id);
    }
}
