package com.example.samsproject.service;

import com.example.samsproject.repository.SamsRepository;
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
    SamsRepository samsRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<Customer> custemersList =new ArrayList<>();

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public ParentDTO getCustomerDetails(String request) throws Exception {
        Parent parent = kafkaConsumerService.listenResult(request );
        log.info("##############Request Json"+request);
        CommonUtils.validInputData(parent);
        custemersList = samsRepository.findByParentId(parent.getParent());
        log.info("*************___________"+custemersList);
        ParentDTO parentDTO = new ParentDTO();
        setParentData(parentDTO, parent);//----set customers details to dto object
        log.info("********FinalOutPut********"+parentDTO);

        checkCountryCode(parentDTO.getCustomers());
        log.info("********validation completed");
        String hashValueOfParentDTO = generateHashFromJson(parentDTO.toString());
        log.info("-------------HashValueOfCustomerList:"+hashValueOfParentDTO);
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
        parentDTO.setCustomers(custemersList);
    }
    /**
     * @param object
     * @return
     * @throws Exception
     */
    public static String generateHashFromJson(String object) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(object);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(jsonString.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * @param data
     * @throws Exception
     * check the country code in the yml file to the country code in the data from db
     */
    public void checkCountryCode(ArrayList<Customer> data) throws Exception {
        Boolean countryFlag;
        List<String> countryListFromDb = new ArrayList<>();
        for (Customer use : custemersList) {//Customers list from db
            String country = use.getCountry();
            countryListFromDb.add(country);
        }
        log.info("Countries list From data " + countryListFromDb);
        for (String inputCountryCode : country_Code) {
            // for (String ele : countryListFromDb) {
            if (countryListFromDb.contains(inputCountryCode)) {
                countryFlag = true;
            } else {
                countryFlag = false;
            }
            //}
        }
        if (countryFlag=true){
            log.info("That country is in the list");
        }else {
            log.info("Mentioned country is not in the list!!!!");
        }
    }

    /**
     * @param id
     * @param newHashValue
     */
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
                existingData.setFlag(true);
                hashRepository.save(existingData);
                System.out.println("Hash value updated.");
            }
        } else {
            HashData hashData = new HashData();
            hashData.setParentId(id);
            hashData.setHashValue(newHashValue);
            hashData.setDate(LocalDateTime.now());
            hashData.setFlag(false);
            hashRepository.save(hashData);
            System.out.println("Hash value inserted.");
        }
    }
}
