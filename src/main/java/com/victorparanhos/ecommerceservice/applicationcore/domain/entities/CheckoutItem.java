package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

public class CheckoutItem {
    private final Product product;
    private final long quantity;
    private final float discount;

    private final long totalAmount;
    private final long totalDiscount;

    public CheckoutItem(Product product, long quantity, float discount) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;

        totalAmount = product.getAmount() * quantity;
        totalDiscount = (long) (product.getAmount() * discount) * quantity;
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

    public long getTotalAmount() {
        return totalAmount;
    }

    public long getTotalDiscount() {
        return totalDiscount;
    }
}
