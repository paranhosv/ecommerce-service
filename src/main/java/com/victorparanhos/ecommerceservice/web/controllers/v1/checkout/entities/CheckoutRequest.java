package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutRequest {

    @JsonProperty("products")
    public List<CheckoutProduct> checkoutProducts;
    
    public CheckoutCommand toCheckoutCommand() {
        List<CheckoutItemCommand> checkoutItems = checkoutProducts.stream().map(p -> new CheckoutItemCommand(
                p.id,
                p.quantity
        )).collect(Collectors.toList());

        return new CheckoutCommand(checkoutItems);
    }
}
