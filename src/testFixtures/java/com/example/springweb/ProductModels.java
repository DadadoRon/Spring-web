package com.example.springweb;

import com.example.springweb.controllers.product.ProductCreateDto;
import com.example.springweb.entity.Product;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductModels {

    public static Product createProduct() {
        int minPrice = 50;
        int maxPrice = 150;
        int price = ThreadLocalRandom.current().nextInt(minPrice, maxPrice);
        BigDecimal bigDecimalPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN);
        return Product.builder()
                .id(null)
                .name(RandomStringUtils.randomAlphabetic(8,12))
                .description(RandomStringUtils.randomAlphabetic(11,15))
                .price(bigDecimalPrice)
                .imageName(RandomStringUtils.randomAlphabetic(5, 6) + ".jpg")
                .build();
    }

    public static List<Product> createRandomProductList() {
        int min = 3;
        int max = 8;
        int productListSize = ThreadLocalRandom.current().nextInt(min, max);
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < productListSize; i++) {
            productList.add(createProduct());
        }
        return productList;
    }

    public static ProductCreateDto createProductDto() {
        int minPrice = 50;
        int maxPrice = 150;
        int price = ThreadLocalRandom.current().nextInt(minPrice, maxPrice);
        BigDecimal bigDecimalPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN);
        return ProductCreateDto.builder()
                .name(RandomStringUtils.randomAlphabetic(8,12))
                .description(RandomStringUtils.randomAlphabetic(11,15))
                .price(bigDecimalPrice)
                .imageName(RandomStringUtils.randomAlphabetic(5, 6) + ".jpg")
                .build();
    }
}
