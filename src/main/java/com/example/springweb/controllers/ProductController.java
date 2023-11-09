package com.example.springweb.controllers;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product findAll(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public Product create(@RequestBody Product product) {

        return productService.createProduct(product);
    }

    @PutMapping("/products")
    public Product update(@RequestBody Product product) {

        return productService.update(product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }
}
