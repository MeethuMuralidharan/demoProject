package com.example.samsproject.service;

import com.example.samsproject.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import com.example.samsproject.dto.ParentDTO;
import com.example.samsproject.model.Customer;
import com.example.samsproject.model.HashData;
import com.example.samsproject.model.Parent;
import com.example.samsproject.repository.HashRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Customer> customers=new ArrayList<>();
        public ParentDTO getUserDetails(String request) throws Exception {

            Parent parent = kafkaConsumerService.listenResult(request );
            log.info("##############Request Json"+request);
            CommonUtils.validInputData(parent);

            List<Customer> custemersList = kafkaConsumerService.findAllTasks();
            log.info("Consumers list ---"+custemersList);
            //----adding customers having same parentId to a list
            for( Customer customer : custemersList){
                if(customer.getParentId().equals(parent.getParent())) {
                    customers.add(kafkaConsumerService.getByUserId(customer.userId));
                }else{
                    log.info("There is no matched record for this id");
                }
            }
            log.info("Customers list:"+customers);
            ParentDTO parentDTO = new ParentDTO();
            setParentData(parentDTO, parent);//----set customers details to dto object
            log.info("********FinalOutPut********"+parentDTO);

            checkCountryCode(parentDTO.getUser());
            log.info("********validation completed");
            String hashValueOfParentDTO = generateHashFromJson(parentDTO.toString());
            log.info("-------------HashValueOfCustomerList:"+hashValueOfParentDTO);
            log.info("**********************************************************"+parentDTO.getParent());

         /*   List<HashData> hashList = hashRepository.findAll();
            List<String> parentIdList = new ArrayList<>();
            boolean flag = false;
            for (HashData li : hashList) {
                parentIdList.add(li.getParentId());
                log.info("List of Id's" + li);
            }
            for(String ids :parentIdList) {
                if (ids.equals(parentDTO.getParent())) {
                    flag = true;
                    log.info("Inside flag" + flag);
                }
            }
            if(flag){
                log.info("Inside the code validation mtd: ");
                checkAndUpdateHashValue(parentDTO.getParent(),hashValueOfParentDTO);
            }else {
                log.info("Inside the else----");
                HashData hashData = new HashData();
                hashData.setParentId(parent.getParent());
                hashData.setHashValue(hashValueOfParentDTO);
                hashData.setDate(LocalDateTime.now());
                hashRepository.save(hashData);
                log.info("Completed insert----");
            }*/

            checkAndUpdateHashValue(parentDTO.getParent(),hashValueOfParentDTO);
            String filePath = "src/main/resources/data.json";
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath),parentDTO);
                System.out.println("JSON file created: " +filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parentDTO;
        }

    private void setParentData(ParentDTO parentDTO, Parent parent) {
        parentDTO.setParent(parent.getParent());
        parentDTO.setName(parent.getName());
        parentDTO.setEmail(parent.getEmail());
        parentDTO.setAddress(parent.getAddress());
        parentDTO.setPhone(parent.getPhone());
        parentDTO.setRegisteredAt(parent.getRegisteredAt());
        parentDTO.setUser(customers);
    }
    public static String generateHashFromJson(String object) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(object);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(jsonString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

/*    public String generateHashFromJson(String jsonString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(jsonString.getBytes());

        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest){
            sb= sb.append(String.format("%02x", b & 0xff));
        }
        return String.valueOf(sb);
    }*/
    //check the country code in the yml file to the country code in the data from db
    public void checkCountryCode(ArrayList<Customer> data){
        List<String> countryListFromDb = new ArrayList<>();
        for (Customer use : customers) {//Customers list from db
            String country = use.getCountry();
            countryListFromDb.add(country);
        }
        log.info("Countries list From data " + countryListFromDb);
        for (String inputCountryCode : country_Code) {
           // for (String ele : countryListFromDb) {
                if (countryListFromDb.contains(inputCountryCode)) {
                    log.info("That country is in the list");
                } else {
                    log.info("Mentioned country is not in the list!!!!");
                }
            //}
        }
    }
    public void checkAndUpdateHashValue(String id, String newHashValue) {
        HashData existingData = hashRepository.findByParentId(id);
        if (existingData != null) {
            log.info("Hashvalue from db: "+existingData.getHashValue());
            log.info("Hashvalue new: "+newHashValue);
            if (existingData.getHashValue().equals(newHashValue)) {
                System.out.println("----Hash value is already the same.");
            } else {
                existingData.setHashValue(newHashValue);
                existingData.setDate(LocalDateTime.now()); // Update the date to the current date
                hashRepository.save(existingData);
                System.out.println("Hash value updated.");
            }
        } else {
            HashData hashData = new HashData();
            hashData.setParentId(id);
            hashData.setHashValue(newHashValue);
            hashData.setDate(LocalDateTime.now());
            hashRepository.save(hashData);
            System.out.println("Hash value inserted.");
        }
    }
/*      HashData existingData  = hashRepository.findByParentId(id);
        log.info("Optional data:"+existingData);
        log.info("Hashvalue from db: "+existingData.getHashValue());
        log.info("Hashvalue new: "+newHashValue);
            if(existingData.getHashValue().equals(newHashValue)){
                System.out.println("Hash value is already the same.");
            }else{
                existingData.setHashValue(newHashValue);
                existingData.setDate(LocalDateTime.now()); // Update the date to the current date
                hashRepository.save(existingData);
                System.out.println("Hash value updated.");
            }
    }*/
}
