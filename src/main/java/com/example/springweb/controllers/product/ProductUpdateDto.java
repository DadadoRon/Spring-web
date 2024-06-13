package com.example.springweb.controllers.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductUpdateDto(
    Integer id,
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50, message = "Name must be at most 50 characters long")
    String name,
    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 200, message = "Description must be at most 200 characters long")
    String description,
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=3, fraction=2)
    BigDecimal price,
    @NotBlank(message = "Image name cannot be empty")
    String imageName
) {}
