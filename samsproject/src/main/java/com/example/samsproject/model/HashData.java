package com.example.samsproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Objects;
@Document(collection = "hashData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashData {
    @Id
    // private String _id;
    private String parentId;
    private String hashValue;
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashData hashData = (HashData) o;
        return Objects.equals(parentId, hashData.parentId) && Objects.equals(hashValue, hashData.hashValue) && Objects.equals(date, hashData.date);
    }
    @Override
    public int hashCode() {
        return Objects.hash(parentId, hashValue, date);
    }
}
