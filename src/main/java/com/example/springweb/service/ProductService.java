package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.repository.ProductRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "products")
@Service
public class ProductService extends BaseService<Product, Integer> {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    @Cacheable(key = "#productId")
    public Product findByIdRequired(Integer productId) {
        return productRepository.findByIdRequired(productId);
    }

    @CachePut(key = "#product.id")
    @CacheEvict(allEntries = true)
    public Product create(Product product) {
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
    public void delete(Integer productId) {
        productRepository.checkIfExistsById(productId);
        super.delete(productId);
    }
}
