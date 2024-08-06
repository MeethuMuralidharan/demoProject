package com.example.samsproject.dto;

import com.example.samsproject.model.Address;
import com.example.samsproject.model.Customer;
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
public class ParentDTO {
//    @JsonProperty("Parent")
  //  public Parent parent;
    public String parent;
    public String name;
    public String email;
    public Address address;
    public String phone;
    public String registeredAt;
    @JsonProperty("User")
    public ArrayList<Customer> user;



}
