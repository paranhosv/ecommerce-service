package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.DiscountServerException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class MakeCheckoutImpl implements MakeCheckout {

    private static final Logger logger = LoggerFactory.getLogger(MakeCheckoutImpl.class);
    private final ProductGateway productsGateway;
    private final DiscountServiceGateway discountServiceGateway;

    public MakeCheckoutImpl(ProductGateway productsGateway, DiscountServiceGateway discountServiceGateway) {
        this.productsGateway = productsGateway;
        this.discountServiceGateway = discountServiceGateway;
    }

    @Override
    public Checkout execute(CheckoutCommand checkoutCmd) throws UnavailableDataException, ProductNotFoundException {
        logger.info("Executing checkout");
        Map<Integer, Long> productAndQuantity = checkoutCmd.getCheckoutItems().stream()
                // Do not process items with no quantity
                .filter(cp -> cp.getQuantity() > 0)
                // Sum item quantity with same product id removing duplicates
                .collect(
                        groupingBy(CheckoutItemCommand::getProductId, summingLong(CheckoutItemCommand::getQuantity))
                );

        logger.info("Getting products data");
        Collection<CheckoutItem> checkoutItems = productsGateway.getProductsById(productAndQuantity.keySet()).parallelStream()
                .map(product -> {
                    var productDiscount = 0F;
                    try {
                        productDiscount = discountServiceGateway.getDiscount(product.getId());
                    } catch (DiscountServerException e) {
                        logger.warn("Discount for productId {} will be zero percent due to discount service exception",
                                product.getId());
                    }

                    return new CheckoutItem(
                            product,
                            productAndQuantity.get(product.getId()),
                            productDiscount);
                })
                .collect(toList());

        logger.info("Checking for not found products");
        var foundProductIds = checkoutItems.stream().map(ci -> ci.getProduct().getId()).collect(toSet());
        if(!foundProductIds.containsAll(productAndQuantity.keySet())) {
            throw new ProductNotFoundException("One or more products were not found");
        }

        logger.info("Returning checkout summary");
        return new Checkout(checkoutItems);
    }
}
