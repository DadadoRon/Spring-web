package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig(cacheNames = "products")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    @Cacheable
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(key = "#productId")
    public Product getProductById(Integer productId) {
        return productRepository.findByIdRequired(productId);
    }

    @Override
    @CachePut(key = "#product.id")
    @CacheEvict(allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CachePut(key = "#product.id")
    @CacheEvict(allEntries = true)
    public Product update(Product product) {
        Integer productId = product.getId();
        productRepository.checkIfExistsById(productId);
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(key = "#productId", allEntries = true)
    public void deleteProduct(Integer productId) {
        productRepository.checkIfExistsById(productId);
        productRepository.deleteById(productId);
    }
}
