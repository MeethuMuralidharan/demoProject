package com.example.samsproject.model;

import com.example.samsproject.dto.PurchaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import java.util.ArrayList;


@Document(collection = "customerData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Customer {

    @Id
    public String _id;
    public String userId;
    public String name;
    public String email;
    public String country;
   /* public Parent parent;*/
    @JsonProperty("ParentId")
    public String parentId;
    public Address address;
    public String phone;
    public String registeredAt;
    public ArrayList<PurchaseDTO> purchases;

}
