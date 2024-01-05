package com.example.springweb.repository;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.exceptions.ProductNotFoundException;
import com.example.springweb.exceptions.UserAppointmentNotFoundException;
import com.example.springweb.exceptions.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

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
