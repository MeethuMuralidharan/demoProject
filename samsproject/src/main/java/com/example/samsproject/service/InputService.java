package com.example.samsproject.service;

import com.example.samsproject.dto.ParentDTO;
import com.example.samsproject.exceptions.ParentException;
import com.example.samsproject.exceptions.UserException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
public interface InputService {
    ParentDTO getUserDetails(String request) throws Exception;
}
