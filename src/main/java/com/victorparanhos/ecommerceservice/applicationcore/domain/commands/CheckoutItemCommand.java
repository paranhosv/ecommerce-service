package com.victorparanhos.ecommerceservice.applicationcore.domain.commands;

public class CheckoutItemCommand {
    private final int productId;
    private final long quantity;

    public CheckoutItemCommand(int id, long quantity) {
        this.productId = id;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}