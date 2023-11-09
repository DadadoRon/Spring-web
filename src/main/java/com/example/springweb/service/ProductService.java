package com.example.springweb.service;

import com.example.springweb.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Integer productId);
    Product createProduct(Product product);
    Product update(Product product);
    void deleteProduct(Integer productId);
}
