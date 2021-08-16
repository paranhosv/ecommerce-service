package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTests {
    private final EasyRandom generator = new EasyRandom();

    @Test
    public void CheckoutShouldCalculateValuesOnConstruction() {
        var items = generator.objects(CheckoutItem.class, 5).collect(Collectors.toList());

        var checkout = new Checkout(items);

        assertThat(checkout.checkoutItems).isEqualTo(items);
        assertThat(checkout.totalAmount).isEqualTo(
                items.stream()
                        .mapToLong(CheckoutItem::getTotalAmount)
                        .sum());
        assertThat(checkout.totalDiscount).isEqualTo(
                items.stream()
                        .mapToLong(CheckoutItem::getTotalDiscount)
                        .sum());
        assertThat(checkout.totalAmountWithDiscount).isEqualTo(checkout.totalAmount - checkout.totalDiscount);
    }
}
