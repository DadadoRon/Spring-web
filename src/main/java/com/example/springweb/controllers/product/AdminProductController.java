package com.example.springweb.controllers.product;

import com.example.springweb.entity.Product;
import com.example.springweb.mapper.ProductMapper;
import com.example.springweb.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.springweb.controllers.product.AdminProductController.REQUEST_MAPPING;

@RestController
@Tag(name = "Products API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class AdminProductController {
    public static final String REQUEST_MAPPING = "/api/v1/admin/products";
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ProductDto create(@Valid @RequestBody ProductCreateDto createDto) {
        Product productForCreate = productMapper.toProductForCreate(createDto);
        Product product = productService.create(productForCreate);
        return productMapper.toDto(product);
    }

    @PutMapping
    public ProductDto update(@Valid @RequestBody ProductUpdateDto updateDto) {
        Product product = productService.update(productMapper.toProductForUpdate(updateDto));
        return productMapper.toDto(product);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        productService.delete(id);
    }
}
