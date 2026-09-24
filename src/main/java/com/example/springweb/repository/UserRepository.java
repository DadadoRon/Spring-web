package com.example.springweb.repository;

import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.ApiErrorCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String userEmail);

   @Query("""
           select u
           from User u
           where lower(u.lastName) like lower(concat(:#{#search.lastName}, '%'))
           and lower(u.email) like lower(concat(:#{#search.email}, '%'))
           and lower(u.firstName) like lower(concat(:#{#search.firstName}, '%'))
           """)
   List<User> findAllByAnyFieldsIgnoreCaseContaining(@Param("search") UserSearch search);

    @Override
    default String entityName() {
        return "User";
    }

    @Override
    default ApiErrorCode notFoundErrorCode() {
        return ApiErrorCode.USER_NOT_FOUND;
    }
}
