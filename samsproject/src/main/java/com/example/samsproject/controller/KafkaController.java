package com.example.samsproject.controller;

import com.example.samsproject.service.InputService;
import com.example.samsproject.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/samsproject")
public class KafkaController {

    private final KafkaProducerService producerService;

    @Autowired
    InputService inputService;
    @Autowired
    public KafkaController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish")
    public ResponseEntity getUsersList(@RequestBody String request) throws Exception {
         producerService.sendMessage("test-topic", request);
         inputService.getCustomerDetails(request);
         return ResponseEntity.ok("Data Added Successfully");


    }
}
