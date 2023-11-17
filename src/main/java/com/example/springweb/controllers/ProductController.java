package com.example.springweb.controllers;

import com.example.springweb.entity.Product;
import com.example.springweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.ProductController.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class ProductController {

    public static final String REQUEST_MAPPING = "/api/v1/products";


    private final ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product findAll(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {

        return productService.createProduct(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) {

        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }
}
