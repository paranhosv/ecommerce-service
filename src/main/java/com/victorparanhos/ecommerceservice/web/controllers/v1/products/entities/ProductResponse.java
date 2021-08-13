package com.victorparanhos.ecommerceservice.web.controllers.v1.products.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;

public class ProductResponse {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("amount")
    public Long amount;

    @JsonProperty("is_gift")
    public boolean isGift;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.amount = product.getAmount();
        this.isGift = product.isGift();
    }
}
