package com.example.samsproject.controller;


import com.example.samsproject.dto.ParentDTO;
import com.example.samsproject.exceptions.ParentException;
import com.example.samsproject.exceptions.UserException;
import com.example.samsproject.model.Customer;
import com.example.samsproject.service.InputService;
import com.example.samsproject.service.KafkaConsumerService;
import com.example.samsproject.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/samsproject")
public class KafkaController {

    private final KafkaProducerService producerService;
    @Autowired
    KafkaConsumerService kafkaConsumerService;
    @Autowired
    InputService inputService;
    @Autowired
    public KafkaController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }
    @GetMapping("/getData")
    public List<Customer> getAllCustomers() {
        List<Customer> customerList = kafkaConsumerService.findAllTasks();
        log.info("**********DBConnected**********" + customerList);
        return customerList;
    }
    @GetMapping("/{id}")
    public Customer getCustomersById(@PathVariable String id) throws IOException {
        return kafkaConsumerService.getByUserId(id);
    }
    @PostMapping("/publish")
    public ResponseEntity getUsersList(@RequestBody String request) throws Exception {
         producerService.sendMessage("test-topic", request);
         inputService.getUserDetails(request);
         return ResponseEntity.ok("Data Added Successfully");


    }
}
