package com.example.samsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
    public String productId;
    public String purchaseDate;
    public int quantity;
    public double totalPrice;
}
