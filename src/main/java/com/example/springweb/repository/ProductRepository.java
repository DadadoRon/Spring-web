package com.example.springweb.repository;

import com.example.springweb.entity.Product;
import com.example.springweb.exceptions.ApiErrorCode;

public interface ProductRepository extends BaseRepository<Product> {

    @Override
    default String entityName() {
        return "Product";
    }

    @Override
    default ApiErrorCode notFoundErrorCode() {
        return ApiErrorCode.PRODUCT_NOT_FOUND;
    }
}
