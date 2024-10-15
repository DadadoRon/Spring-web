package com.example.springweb.controllers.product;

import java.math.BigDecimal;

public record ProductDto(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        String imageName
) { }
