package com.example.springweb.controllers.product;

import com.example.springweb.entity.Product;
import com.example.springweb.mapper.ProductMapper;
import com.example.springweb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.product.ProductController.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class ProductController {

    public static final String REQUEST_MAPPING = "/api/v1/products";


    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> findAll() {
        List<ProductDto> products = productService.getAllProducts().stream()
                .map(it -> productMapper.toDto(it))
                .toList();
        return products;
    }

    @GetMapping("/{id}")
    public ProductDto findAll(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return productMapper.toDto(product);
    }

    @PostMapping
    public ProductDto create(@RequestBody ProductCreateDto createDto) {
        Product productForCreate = productMapper.toProductForCreate(createDto);
        Product product = productService.createProduct(productForCreate);
        return productMapper.toDto(product);
    }

    @PutMapping
    public ProductDto update(@RequestBody ProductUpdateDto updateDto) {
        Product product = productService.update(productMapper.toProductForUpdate(updateDto));
        return productMapper.toDto(product);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {

        productService.deleteProduct(id);
    }


//    public ProductDto toDto(Product product) {
//        return ProductDto.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .price(product.getPrice())
//                .imageName(product.getImageName())
//                .build();
//    }
//    public Product toProductForCreate(ProductCreateDto createDto) {
//        return Product.builder()
//                .name(createDto.getName())
//                .description(createDto.getDescription())
//                .price(createDto.getPrice())
//                .imageName(createDto.getImageName())
//                .build();
//    }
//    public Product toProductForUpdate (ProductUpdateDto updateDto) {
//        return Product.builder()
//                .id(updateDto.getId())
//                .name(updateDto.getName())
//                .description(updateDto.getDescription())
//                .price(updateDto.getPrice())
//                .imageName(updateDto.getImageName())
//                .build();
//    }



}
