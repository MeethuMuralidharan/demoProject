package com.example.samsproject.util;

import com.example.samsproject.dto.ParentDTO;
import com.example.samsproject.exceptions.GlobalExceptionHandler;
import com.example.samsproject.exceptions.ParentException;
import com.example.samsproject.model.Customer;
import com.example.samsproject.model.Parent;
import com.example.samsproject.service.InputServiceImpl;
import com.example.samsproject.service.KafkaConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CommonUtils {
    /*@Value("${country.code}")
    public static String code;*/
    @Value("${country.code}")
    public String[] country_Code;
    ArrayList<Customer> customers = new ArrayList<>();

    public static void validInputData(Parent parent) throws ParentException {
        log.info("Inside validInput data");
        if (parent.getParent() == null) {
            throw new ParentException("ParentCode can't be null or empty");
        } else if (parent.getName() == null) {
            throw new ParentException("Name can't be null or empty");
        }
    }

    public void checkCountryCode(ArrayList<Customer> data) {
        List<String> result = new ArrayList<>();
        for (Customer use : customers) {
            String country = use.getCountry();
            result.add(country);
        }
        log.info("Result" + result);
        for (String element : country_Code) {
            for(String ele :result){
                if (ele.contains(element)) {
                    log.info("That country is in the list!!!!");
                }
            }
        }
    }
}
