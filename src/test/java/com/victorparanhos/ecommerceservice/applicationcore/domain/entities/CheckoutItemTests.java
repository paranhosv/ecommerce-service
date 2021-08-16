package com.victorparanhos.ecommerceservice.applicationcore.domain.entities;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutItemTests {
    private final EasyRandom generator = new EasyRandom();

    @Test
    public void CheckoutItemShouldBeCalculateValuesOnConstruction() {
        var item = new CheckoutItem(
                generator.nextObject(Product.class),
                generator.nextLong(),
                generator.nextFloat()
        );

        assertThat(item.getTotalAmount()).isEqualTo(item.getProduct().getAmount() * item.getQuantity());
        assertThat(item.getTotalDiscount()).isEqualTo(
                (long) (item.getProduct().getAmount() * item.getDiscount()) * item.getQuantity());
    }
}
