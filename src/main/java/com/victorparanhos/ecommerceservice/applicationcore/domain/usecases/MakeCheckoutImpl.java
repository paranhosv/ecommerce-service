package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.DiscountServerException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.EmptyBasketException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.*;

public class MakeCheckoutImpl implements MakeCheckout {

    private static final Logger logger = LoggerFactory.getLogger(MakeCheckoutImpl.class);
    private final ProductGateway productsGateway;
    private final DiscountServiceGateway discountServiceGateway;
    private final Set<Date> blackFridays;

    public MakeCheckoutImpl(ProductGateway productsGateway,
                            DiscountServiceGateway discountServiceGateway,
                            Set<Date> blackFridays) {
        this.productsGateway = productsGateway;
        this.discountServiceGateway = discountServiceGateway;
        this.blackFridays = blackFridays;
    }

    @Override
    public Checkout execute(CheckoutCommand checkoutCmd) throws UnavailableDataException,
            ProductNotFoundException, EmptyBasketException {
        logger.info("Executing checkout");
        var productsAndQuantities = normalizeCheckoutCommand(checkoutCmd);
        if(productsAndQuantities.isEmpty()) {
            throw new EmptyBasketException("Must have at least one product in the basket to checkout");
        }

        logger.info("Building checkout items retrieving products data and discounts");
        var checkoutItems = buildCheckoutItems(productsAndQuantities);

        logger.info("Checking for not found products");
        var cmdProductIds = productsAndQuantities.keySet();
        if(!hasFoundAllProducts(cmdProductIds, checkoutItems)) {
            throw new ProductNotFoundException("One or more products were not found");
        }

        addGiftTo(checkoutItems);

        logger.info("Returning checkout summary");
        return new Checkout(checkoutItems);
    }

    private Map<Integer, Long> normalizeCheckoutCommand(CheckoutCommand checkoutCmd) {
        return checkoutCmd.getCheckoutItems().stream()
                // Do not process items with no quantity
                .filter(cp -> cp.getQuantity() > 0)
                // Sum item quantity with same product id removing duplicates
                .collect(
                        groupingBy(CheckoutItemCommand::getProductId, summingLong(CheckoutItemCommand::getQuantity))
                );
    }

    private Collection<CheckoutItem> buildCheckoutItems(Map<Integer, Long> productsAndQuantities)
            throws UnavailableDataException {
        return productsGateway.getProductsById(productsAndQuantities.keySet()).parallelStream()
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
                            productsAndQuantities.get(product.getId()),
                            productDiscount);
                })
                .collect(toList());
    }

    private boolean hasFoundAllProducts(Collection<Integer> cmdProductIds, Collection<CheckoutItem> checkoutItems) {
        var foundProductIds = checkoutItems.stream()
                .map(ci -> ci.getProduct().getId())
                .collect(toSet());
        return foundProductIds.containsAll(cmdProductIds);
    }

    private void addGiftTo(Collection<CheckoutItem> checkoutItems) throws UnavailableDataException {
        var calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        var today = calendar.getTime();
        if(blackFridays.contains(today)) {
            logger.info("Today is black friday! Adding a random gift if any available in stock!");
            Random rand = new Random();
            var giftProducts = productsGateway.getGifts();
            giftProducts.stream()
                    .skip(rand.nextInt(giftProducts.size()))
                    .findFirst().ifPresent(randomGift ->
                            checkoutItems.add(new CheckoutItem(
                                    randomGift,
                                    1,
                                    0.0F)));

        }
    }
}
