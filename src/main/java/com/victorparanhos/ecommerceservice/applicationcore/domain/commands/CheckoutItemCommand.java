package com.victorparanhos.ecommerceservice.applicationcore.domain.commands;

public class CheckoutItemCommand {
    private final long productId;
    private final long quantity;

    public CheckoutItemCommand(long id, long quantity) {
        this.productId = id;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}