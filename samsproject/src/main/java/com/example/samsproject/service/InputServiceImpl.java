package com.example.samsproject.service;


import com.example.samsproject.dto.ParentDTO;
import com.example.samsproject.exceptions.ParentException;
import com.example.samsproject.model.Customer;
import com.example.samsproject.model.HashData;
import com.example.samsproject.model.Parent;
import com.example.samsproject.repository.HashRepository;
import com.example.samsproject.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InputServiceImpl implements InputService{
        @Value("${country.code}")
        public String[] country_Code;
        @Autowired
        KafkaConsumerService kafkaConsumerService;
        @Autowired
        HashRepository hashRepository;
        @Autowired
        Customer customer;
        @Autowired
        CommonUtils commonutils;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Customer> customers = new ArrayList<>();

    //Output Json
        public ParentDTO getUserDetails(String request) throws ParentException, IOException, NoSuchAlgorithmException {
            Parent parent = kafkaConsumerService.listenKafka(request);
            CommonUtils.validInputData(parent);
            String parentIdKafka= parent.getParent();
            log.info("------Parent id from kafkaInputTopic :"+parentIdKafka);
            String hashValueOfParentfromKafka = generateHashFromJson(parentIdKafka);
            log.info("-------------HashValueOfParent:"+hashValueOfParentfromKafka);
            List<Customer> custemersList = kafkaConsumerService.findAllTasks();
            log.info("Consumers list ---"+custemersList);
            for( Customer customer : custemersList){
                if(customer.getParentId().equals(parentIdKafka)) {
                    customers.add(kafkaConsumerService.getByUserId(customer.userId));
                }else{
                    log.info("There is no matched record");
                }
            }
            log.info("Customers list:"+customers);
            ParentDTO parentDTO = new ParentDTO();
            parentDTO.setParent(parent.getParent());
            parentDTO.setName(parent.getName());
            parentDTO.setEmail(parent.getEmail());
            parentDTO.setAddress(parent.getAddress());
            parentDTO.setPhone(parent.getPhone());
            parentDTO.setRegisteredAt(parent.getRegisteredAt());
            parentDTO.setUser(customers);
            log.info("********FinalOutPut********"+parentDTO);
            parentDTO.setUser(customers);
            ArrayList<Customer>cusmerList=parentDTO.getUser();
            log.info("Cus List"+cusmerList);
            String customerParentId=null;
            for(Customer customerId:cusmerList){
                customerParentId = customerId.getParentId();
                customerId.setParent(parent);
            }
            log.info("usersParentId********:"+customerParentId);
            String hashValueOfCustomerListParent = generateHashFromJson(customerParentId);
            log.info("-------------HashValueOfCustomerList:"+hashValueOfCustomerListParent);
            log.info("********validation completed");
            commonutils.checkCountryCode(parentDTO.getUser());
            List<String> userId = new ArrayList<>();
            for(Customer id : cusmerList){
                userId.add(id.getUserId());
            }
            log.info("------UserId is :"+userId);
            if(hashValueOfParentfromKafka.equals(hashValueOfCustomerListParent)){
                HashData hashData = new HashData();
                hashData.setParentId(parent.getParent());
                hashData.setUserId(userId);
                hashData.setHashValue(hashValueOfParentfromKafka);
                hashRepository.insert(hashData);
                log.info("HashValue Data Added Successfully");
            }
            String filePath = "src/main/resources/data.json";
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath),parentDTO);
                System.out.println("JSON file created: " +filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parentDTO;
        }

    public String generateHashFromJson(String jsonString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(jsonString.getBytes());

        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest){
            sb= sb.append(String.format("%02x", b & 0xff));
        }
        return String.valueOf(sb);
    }
/*    public void checkCountryCode(ArrayList<Customer> data) {
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
    }*/
}
