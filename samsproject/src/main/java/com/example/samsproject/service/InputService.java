package com.example.samsproject.service;

import com.example.samsproject.dto.ParentDTO;
import org.springframework.stereotype.Service;

@Service
public interface InputService {
    ParentDTO getCustomerDetails(String request) throws Exception;
}
