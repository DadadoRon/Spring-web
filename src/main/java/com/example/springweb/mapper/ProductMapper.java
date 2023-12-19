package com.example.springweb.mapper;

import com.example.springweb.controllers.product.ProductCreateDto;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.product.ProductUpdateDto;
import com.example.springweb.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toProductForCreate(ProductCreateDto createDto);
    Product toProductForUpdate (ProductUpdateDto updateDto);

}
