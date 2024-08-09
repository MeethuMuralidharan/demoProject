package com.example.samsproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    public String productId;
    public String name;
    public String description;
    public double price;
    public String category;
    public String manufacturer;
    public String purchaseDate;
    public int quantity;
    public double totalPrice;
}
