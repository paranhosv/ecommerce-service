package com.victorparanhos.ecommerceservice.infra.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;

public class JsonFileProduct {

    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("amount")
    public long amount;

    @JsonProperty("is_gift")
    public boolean isGift;

    public Product toProduct(boolean isGift) {
        if(isGift) {
            return new Product(
                    id,
                    title,
                    description,
                    0L,
                    true
            );
        }

        return new Product(
                id,
                title,
                description,
                amount,
                false
        );
    }
}
