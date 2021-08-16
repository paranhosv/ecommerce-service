package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutProduct {

    @JsonProperty("id")
    public int id;

    @JsonProperty("quantity")
    public long quantity;
}
