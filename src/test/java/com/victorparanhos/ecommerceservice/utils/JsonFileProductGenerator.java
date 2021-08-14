package com.victorparanhos.ecommerceservice.utils;

import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;

import java.util.Collection;
import java.util.List;

public class JsonFileProductGenerator {
    public static Collection<JsonFileProduct> jsonFileProductsToList() {
        JsonFileProduct product1 = new JsonFileProduct();
        product1.id = 1;
        product1.title = "Title One";
        product1.description = "Description One";
        product1.amount = 10_000L;
        product1.isGift = false;

        JsonFileProduct product2 = new JsonFileProduct();
        product2.id = 2;
        product2.title = "Title Two";
        product2.description = "Description Two";
        product2.amount = 20_000L;
        product2.isGift = true;

        return List.of(product1, product2);
    }
}
