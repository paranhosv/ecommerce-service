package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

public class Product {
    private final int id;
    private final String title;
    private final String description;
    private final long amount;
    private final boolean isGift;

    public Product(int id, String title, String description, long amount, boolean isGift) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.isGift = isGift;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isGift() {
        return isGift;
    }
}
