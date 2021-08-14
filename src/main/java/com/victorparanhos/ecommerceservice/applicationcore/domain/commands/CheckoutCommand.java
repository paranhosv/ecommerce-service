package com.victorparanhos.ecommerceservice.applicationcore.domain.commands;

import java.util.List;

public class CheckoutCommand {
    private final List<CheckoutItemCommand> checkoutItems;

    public CheckoutCommand(List<CheckoutItemCommand> checkoutItems) {
        this.checkoutItems = checkoutItems;
    }

    public List<CheckoutItemCommand> getCheckoutItems() {
        return checkoutItems;
    }
}
