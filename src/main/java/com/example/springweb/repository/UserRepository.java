package com.example.springweb.repository;


import com.example.springweb.entity.User;
import com.example.springweb.exceptions.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userEmail);

    default User findByIdRequired(Integer id) {
        return findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    default void checkIfExistsById(Integer id) {
        if (!existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }
}
