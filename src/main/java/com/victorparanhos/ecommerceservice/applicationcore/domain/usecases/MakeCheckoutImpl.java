package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class MakeCheckoutImpl implements MakeCheckout {

    private final ProductGateway productsGateway;

    public MakeCheckoutImpl(ProductGateway productsGateway) {
        this.productsGateway = productsGateway;
    }

    @Override
    public Checkout execute(CheckoutCommand checkoutCmd) throws UnavailableDataException, ProductNotFoundException {
        Map<Long, Long> productAndQuantity = checkoutCmd.getCheckoutItems().stream()
                // Do not process items with no quantity
                .filter(cp -> cp.getQuantity() > 0)
                // Sum item quantity with same product id removing duplicates
                .collect(
                        groupingBy(CheckoutItemCommand::getProductId, summingLong(CheckoutItemCommand::getQuantity))
                );

        Collection<CheckoutItem> checkoutItems = productsGateway.getProductsById(productAndQuantity.keySet()).stream()
                .map(product -> new CheckoutItem(product, productAndQuantity.get(product.getId())))
                .collect(toList());

        // Check if checkout has not found products
        var foundProductIds = checkoutItems.stream().map(ci -> ci.getProduct().getId()).collect(toSet());
        if(!foundProductIds.containsAll(productAndQuantity.keySet())) {
            throw new ProductNotFoundException("One or more products were not found");
        }

        return new Checkout(checkoutItems);
    }
}
