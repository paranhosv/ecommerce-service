package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;

public class CheckoutProductResponse {

    @JsonProperty("id")
    public int id;

    @JsonProperty("quantity")
    public long quantity;

    @JsonProperty("unit_amount")
    public long unitAmount;

    @JsonProperty("total_amount")
    public long totalAmount;

    @JsonProperty("discount")
    public long discount;

    @JsonProperty("is_gift")
    public boolean isGift;

    public CheckoutProductResponse(CheckoutItem checkoutItem) {
        this.id = checkoutItem.getProduct().getId();
        this.quantity = checkoutItem.getQuantity();
        this.unitAmount = checkoutItem.getProduct().getAmount();
        this.totalAmount = checkoutItem.getTotalAmount();
        this.discount = checkoutItem.getTotalDiscount();
        this.isGift = checkoutItem.getProduct().isGift();
    }
}
