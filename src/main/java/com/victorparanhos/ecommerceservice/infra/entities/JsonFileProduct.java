package com.victorparanhos.ecommerceservice.infra.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;

public class JsonFileProduct {

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

    public Product toProduct() {
        return new Product(
                id,
                title,
                description,
                amount,
                isGift
        );
    }
}
