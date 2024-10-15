package com.example.springweb.repository;

import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userEmail);

   @Query("""
           select u 
           from User u 
           where lower(u.lastName) like lower(concat(:#{#search.lastName}, '%')) 
           and lower(u.email) like lower(concat(:#{#search.email}, '%')) 
           and lower(u.firstName) like lower(concat(:#{#search.firstName}, '%')) 
           """)
   List<User> findAllByAnyFieldsIgnoreCaseContaining(@Param("search") UserSearch search);

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
