package com.example.springweb.repository;

import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    String entityName();

    ApiErrorCode notFoundErrorCode();

    default T findByIdRequired(Integer id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName() + " not found with id: " + id,
                        notFoundErrorCode()));
    }

    default void checkIfExistsById(Integer id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException(entityName() + " not found with id: " + id, notFoundErrorCode());
        }
    }
}
