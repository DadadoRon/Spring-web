package com.example.springweb.controllers.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {
    private String name;
    private String description;
    private double price;
    private String imageName;
}
