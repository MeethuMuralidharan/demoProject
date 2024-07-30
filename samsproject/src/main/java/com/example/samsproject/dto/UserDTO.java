package com.example.samsproject.dto;

import com.example.samsproject.model.Parent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserDTO {
    public String _id;
    public String name;
    public String userId;
    public String email;
    public String country;
    @JsonProperty("ParentId")
    public String parentId;
    @JsonProperty("Parent")
    public Parent parent;
    public AddressDTO address;
    public String phone;
    public String registeredAt;
    public ArrayList<PurchaseDTO> purchases;
}
