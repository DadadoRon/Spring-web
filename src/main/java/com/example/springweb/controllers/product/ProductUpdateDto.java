package com.example.springweb.controllers.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDto {
    private Integer id;
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50, message = "Name must be at most 50 characters long")
    private String name;
    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 200, message = "Description must be at most 200 characters long")
    private String description;
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=3, fraction=2)
    private double price;
    @NotBlank(message = "Image name cannot be empty")
    private String imageName;
}
