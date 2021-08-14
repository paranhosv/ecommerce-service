package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

public class CheckoutItem {
    private final Product product;
    private final long quantity;

    public CheckoutItem(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
