package com.example.springweb.repository;

import com.example.springweb.entity.Product;
import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    default Product findByIdRequired(Integer id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id,
                        ApiErrorCode.PRODUCT_NOT_FOUND));
    }

    default void checkIfExistsById(Integer id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id, ApiErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}
