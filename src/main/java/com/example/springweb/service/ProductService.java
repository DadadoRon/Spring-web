package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.repository.ProductRepository;
import org.springframework.cache.annotation.CacheConfig;
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

    @Override
    public Product create(Product product) {
        return super.create(product);
    }

    @Override
    public Product update(Product product) {
        Integer productId = product.getId();
        productRepository.checkIfExistsById(productId);
        return super.update(product);
    }

    @Override
    public void delete(Integer productId) {
        productRepository.checkIfExistsById(productId);
        super.delete(productId);
    }
}
