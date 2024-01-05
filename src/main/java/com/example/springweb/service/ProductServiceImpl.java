package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.exceptions.ProductNotFoundException;
import com.example.springweb.exceptions.UserAppointmentNotFoundException;
import com.example.springweb.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findByIdRequired(productId);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        Integer productId = product.getId();
        productRepository.checkIfExistsById(productId);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.checkIfExistsById(productId);
        productRepository.deleteById(productId);
    }
}
