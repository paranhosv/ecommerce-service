package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.DiscountServerException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.EmptyBasketException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@SpringBootTest
public class MakeCheckoutImplTests {
    private final ProductGateway productGateway = mock(ProductGateway.class);
    private final DiscountServiceGateway discountServiceGateway = mock(DiscountServiceGateway.class);
    private final Set<LocalDate> blackFridays = new HashSet<>();

    private final MakeCheckoutImpl useCase = new MakeCheckoutImpl(productGateway, discountServiceGateway, blackFridays);

    @BeforeEach
    public void resetBlackFriday() {
        blackFridays.clear();
    }

    @Test
    public void executeShouldReturnAllProducts() throws UnavailableDataException, ProductNotFoundException,
            DiscountServerException, EmptyBasketException {
        var expectedProductOne = new Product(1, "Title One", "Description One", 10_000L, false);
        var expectedProductTwo = new Product(2, "Title Two", "Description Two", 20_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProductOne, expectedProductTwo));
        given(discountServiceGateway.getDiscount(anyInt())).willReturn(0.1F);
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(2, 3)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProductOne,
                        1,
                        0.1F
                ),
                new CheckoutItem(
                        expectedProductTwo,
                        3,
                        0.1F
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        then(discountServiceGateway)
                .should(times(2))
                .getDiscount(anyInt());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldReturnAllProductsAndGiftOnBlackFridays() throws UnavailableDataException,
            ProductNotFoundException, DiscountServerException, EmptyBasketException {
        blackFridays.add(LocalDate.now());
        var expectedProductOne = new Product(1, "Title One", "Description One", 10_000L, false);
        var expectedProductTwo = new Product(2, "Title Two", "Description Two", 0L, true);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProductOne));
        given(discountServiceGateway.getDiscount(anyInt())).willReturn(0.1F);
        given(productGateway.getGifts()).willReturn(List.of(expectedProductTwo));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProductOne,
                        1,
                        0.1F
                ),
                new CheckoutItem(
                        expectedProductTwo,
                        1,
                        0.0F
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        then(discountServiceGateway)
                .should(times(1))
                .getDiscount(anyInt());
        then(productGateway)
                .should(times(1))
                .getGifts();
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldNotProcessProductsWithQuantity() throws UnavailableDataException, ProductNotFoundException,
            DiscountServerException, EmptyBasketException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        var unexpectedProduct = new Product(2, "Title Two", "Description Two", 20_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        given(discountServiceGateway.getDiscount(anyInt())).willReturn(0.1F);
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(2, 0)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        1,
                        0.1F
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        then(discountServiceGateway)
                .should(times(1))
                .getDiscount(anyInt());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
        assertThat(checkoutResult.checkoutItems).noneMatch(ci -> ci.getProduct().getId() == unexpectedProduct.getId());
    }

    @Test
    public void executeShouldNotApplyDiscountOnDiscountServiceError() throws UnavailableDataException,
            ProductNotFoundException, DiscountServerException, EmptyBasketException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        given(discountServiceGateway.getDiscount(anyInt())).willThrow(new DiscountServerException("Error"));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(2, 0)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        1,
                        0.0F
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        then(discountServiceGateway)
                .should(times(1))
                .getDiscount(anyInt());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldSumQuantitiesForDuplicateProducts() throws UnavailableDataException,
            ProductNotFoundException, DiscountServerException, EmptyBasketException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        given(discountServiceGateway.getDiscount(anyInt())).willReturn(0.1F);
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(1, 2)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        3,
                        0.1F
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        then(discountServiceGateway)
                .should(times(1))
                .getDiscount(anyInt());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldThrowProductNotFoundExceptionWhenOneOfTheProductsDoesNotExist()
            throws UnavailableDataException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(10, 2)
        ));

        assertThatThrownBy(() ->
                useCase.execute(cmd)
        ).isInstanceOf(ProductNotFoundException.class);
        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
    }

    @Test
    public void executeShouldThrowEmptyBasketExceptionWhenThereIsNoProductsInTheBasket() {
       var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 0),
                new CheckoutItemCommand(10, 0)
        ));

        assertThatThrownBy(() ->
                useCase.execute(cmd)
        ).isInstanceOf(EmptyBasketException.class);
    }
}
