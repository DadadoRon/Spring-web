package com.example.springweb.controllers.product;

import com.example.springweb.entity.Product;
import com.example.springweb.mapper.ProductMapper;
import com.example.springweb.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.product.ProductController.REQUEST_MAPPING;


@RestController
@Tag(name = "Products API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class ProductController {

    public static final String REQUEST_MAPPING = "/api/v1/products";
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> findAll() {
        return productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto findAll(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return productMapper.toDto(product);
    }

    @PostMapping
    public ProductDto create(@Valid  @RequestBody ProductCreateDto createDto) {
        Product productForCreate = productMapper.toProductForCreate(createDto);
        Product product = productService.createProduct(productForCreate);
        return productMapper.toDto(product);
    }

    @PutMapping
    public ProductDto update(@Valid @RequestBody ProductUpdateDto updateDto) {
        Product product = productService.update(productMapper.toProductForUpdate(updateDto));
        return productMapper.toDto(product);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {

        productService.deleteProduct(id);
    }
}
