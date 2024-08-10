package com.example.springweb.repository;

import com.example.springweb.entity.Product;
import com.example.springweb.exceptions.ProductNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    default Product findByIdRequired(Integer id) {
        return findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    default void checkIfExistsById(Integer id) {
        if (!existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }
}
