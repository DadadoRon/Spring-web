package com.example.springweb.controllers.product;

import com.example.springweb.entity.Product;
import com.example.springweb.mapper.ProductMapper;
import com.example.springweb.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.springweb.controllers.product.CommonProductController.REQUEST_MAPPING;


@RestController
@Tag(name = "Products API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class CommonProductController {
    public static final String REQUEST_MAPPING = "/api/v1/common/products";
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
}
