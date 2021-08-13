package com.victorparanhos.ecommerceservice.utils;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;

import java.util.List;

public class ProductGenerator {
    public static List<Product> generateProductList() {
        Product product1 = new Product(
                1,
                "Title One",
                "Description One",
                10_000L,
                false);

        Product product2 = new Product(
                2,
                "Title Two",
                "Description Two",
                20_000L,
                true);

        return List.of(product1, product2);
    }
}
