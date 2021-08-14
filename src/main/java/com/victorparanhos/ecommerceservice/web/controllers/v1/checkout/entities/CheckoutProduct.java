package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutProduct {

    @JsonProperty("id")
    public long id;

    @JsonProperty("quantity")
    public long quantity;
}
