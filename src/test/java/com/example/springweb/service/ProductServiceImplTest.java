package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.exceptions.ProductNotFoundException;
import com.example.springweb.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private final Integer testProductId = 1;

    private final Product testProduct = Product.builder()
            .id(testProductId)
            .name("Чистка")
            .description("Чистка лица")
            .imageName("img/n4.jpg")
            .price(BigDecimal.valueOf(30.10))
            .build();

    @Test
    void productByIdTest() {
        when(productRepository.findByIdRequired(testProductId)).thenReturn(testProduct);
        Product result = productService.getProductById(testProductId);
        assertNotNull(result);
        assertEquals(testProduct.getId(), result.getId());
        verify(productRepository, times(1)).findByIdRequired(testProductId);
    }

    @Test
    void productByIdNotFoundTest() {
        when(productRepository.findByIdRequired(testProductId))
                .thenThrow(new ProductNotFoundException("Product not found with id:" + testProductId));
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(testProductId));
        verify(productRepository, times(1)).findByIdRequired(testProductId);
    }

    @Test
    void createProductTest() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        Product result = productService.createProduct(testProduct);
        assertEquals(testProduct, result);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void updateTest() {
        doNothing().when(productRepository).checkIfExistsById(testProduct.getId());
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        Product result = productService.update(testProduct);
        assertEquals(testProduct.getId(), result.getId());
        verify(productRepository, times(1)).checkIfExistsById(testProductId);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void updateIfNotFoundTest() {
        doThrow(new ProductNotFoundException("Product not found with id: " + testProduct.getId()))
                .when(productRepository).checkIfExistsById(testProduct.getId());
        assertThrows(ProductNotFoundException.class, () -> productService.update(testProduct));
    }

    @Test
    void deleteProductTest() {
        doNothing().when(productRepository).checkIfExistsById(testProductId);
        doNothing().when(productRepository).deleteById(testProductId);
        productService.deleteProduct(testProductId);
    }

    @Test
    void deleteProductIfIdNotFoundTest() {
        doThrow(new ProductNotFoundException("Product not found with id: " + testProductId))
                .when(productRepository).checkIfExistsById(testProductId);
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(testProductId));
    }
}
