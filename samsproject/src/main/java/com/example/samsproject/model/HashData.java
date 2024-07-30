package com.example.samsproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "hashData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashData {

    @Id
    public String id;
    public String parentId;
    public List<String> userId;
    public String hashValue;
}
