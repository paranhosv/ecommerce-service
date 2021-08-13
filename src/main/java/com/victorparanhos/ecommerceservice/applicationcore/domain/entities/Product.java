package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

public class Product {
    private final Integer id;
    private final String title;
    private final String description;
    private final Long amount;
    private final boolean isGift;

    public Product(Integer id, String title, String description, Long amount, boolean isGift) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.isGift = isGift;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getAmount() {
        return amount;
    }

    public boolean isGift() {
        return isGift;
    }
}
