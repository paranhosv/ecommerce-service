package com.victorparanhos.ecommerceservice.web.controllers.v1.products.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductResponse {

    @JsonProperty("id")
    public long id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("amount")
    public long amount;

    @JsonProperty("is_gift")
    public boolean isGift;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.amount = product.getAmount();
        this.isGift = product.isGift();
    }

    public static List<ProductResponse> toProductsResponse(List<Product> products) {
        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }
}
