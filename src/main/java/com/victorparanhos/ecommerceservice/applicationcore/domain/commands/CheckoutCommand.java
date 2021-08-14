package com.victorparanhos.ecommerceservice.applicationcore.domain.commands;

import java.util.Collection;

public class CheckoutCommand {
    private final Collection<CheckoutItemCommand> checkoutItems;

    public CheckoutCommand(Collection<CheckoutItemCommand> checkoutItems) {
        this.checkoutItems = checkoutItems;
    }

    public Collection<CheckoutItemCommand> getCheckoutItems() {
        return checkoutItems;
    }
}
