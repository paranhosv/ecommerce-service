package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

import java.util.Collection;

public class Checkout {

    public final long totalAmount;
    public final long totalAmountWithDiscount;
    public final long totalDiscount;
    public Collection<CheckoutItem> checkoutItems;

    public Checkout(Collection<CheckoutItem> checkoutItems) {
        this.checkoutItems = checkoutItems;
        totalAmount = checkoutItems.stream()
                .mapToLong(checkoutItem -> checkoutItem.getProduct().getAmount() * checkoutItem.getQuantity())
                .reduce(0, Long::sum);

        totalDiscount = 0;
        totalAmountWithDiscount = totalAmount - totalDiscount;
    }
}
