package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

public class CheckoutItem {
    private final Product product;
    private final long quantity;
    private final float discount;

    public CheckoutItem(Product product, long quantity, float discount) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public float getDiscount() {
        return discount;
    }
}
