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
                .mapToLong(CheckoutItem::getTotalAmount)
                .sum();

        totalDiscount = checkoutItems.stream()
                .mapToLong(CheckoutItem::getTotalDiscount)
                .sum();

        totalAmountWithDiscount = totalAmount - totalDiscount;
    }
}
