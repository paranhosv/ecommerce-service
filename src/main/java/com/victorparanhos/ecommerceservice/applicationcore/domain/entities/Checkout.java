package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

import java.util.List;

public class Checkout {

    public final long totalAmount;
    public final long totalAmountWithDiscount;
    public final long totalDiscount;
    public List<CheckoutItem> checkoutItems;

    public Checkout(List<CheckoutItem> checkoutItems) {
        this.checkoutItems = checkoutItems;
        totalAmount = checkoutItems.stream()
                .mapToLong(checkoutItem -> checkoutItem.getProduct().getAmount() * checkoutItem.getQuantity())
                .reduce(0, Long::sum);

        totalDiscount = 0;
        totalAmountWithDiscount = totalAmount - totalDiscount;
    }
}
