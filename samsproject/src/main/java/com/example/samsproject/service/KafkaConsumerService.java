package com.example.samsproject.service;

import com.example.samsproject.model.Customer;
import com.example.samsproject.model.Parent;
import com.example.samsproject.repository.SamsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@Component
public class KafkaConsumerService {

    @Autowired
    SamsRepository samsRepository;
//    @Value("${spring.consumer.topic}")
//    public String test_topic;

    @KafkaListener(topics = "${spring.consumer.topic}" , groupId = "my-group")
    public Parent listenKafka(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Parent parentData = objectMapper.readValue(message, Parent.class);
        log.info("---------new Parent---------"+parentData);
        return parentData;
    }
    public List<Customer> findAllTasks() {
        log.info("*****Data from DB");
        List<Customer> list = samsRepository.findAll();
        log.info("full records from db"+list);
        return samsRepository.findAll();
    }
    public Customer getByUserId(String id) {
        return samsRepository.findByUserId(id);
    }
}
