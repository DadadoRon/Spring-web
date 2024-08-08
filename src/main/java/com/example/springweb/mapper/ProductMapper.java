package com.example.springweb.mapper;

import com.example.springweb.controllers.product.ProductCreateDto;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.product.ProductUpdateDto;
import com.example.springweb.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    @Mapping(target = "id", ignore = true)
    Product toProductForCreate(ProductCreateDto createDto);
    Product toProductForUpdate (ProductUpdateDto updateDto);
}
