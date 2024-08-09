package com.example.samsproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Parent {
    @JsonProperty("Parent")
    public String parent;
    public String name;
    public String email;
    public Address address;
    public String phone;
    public String registeredAt;
}
