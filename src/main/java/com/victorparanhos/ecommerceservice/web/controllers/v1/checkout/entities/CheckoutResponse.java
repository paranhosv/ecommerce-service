package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class CheckoutResponse {

    @JsonProperty("total_amount")
    public long totalAmount;

    @JsonProperty("total_amount_with_discount")
    public long totalAmountWithDiscount;

    @JsonProperty("total_discount")
    public long totalDiscount;

    @JsonProperty("products")
    public Collection<CheckoutProductResponse> productsResponse;

    private CheckoutResponse(Checkout checkout) {
        totalAmount = checkout.totalAmount;
        totalAmountWithDiscount = checkout.totalAmountWithDiscount;
        totalDiscount = checkout.totalDiscount;
        productsResponse = checkout.checkoutItems.stream()
                .map(CheckoutProductResponse::new)
                .collect(toList());

    }

    public static CheckoutResponse toCheckoutResponse(Checkout checkout) {
        return new CheckoutResponse(checkout);
    }
}
