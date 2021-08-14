package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutItemCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.CheckoutItem;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@SpringBootTest
public class MakeCheckoutImplTests {
    private final ProductGateway productGateway = mock(ProductGateway.class);
    private final MakeCheckoutImpl useCase = new MakeCheckoutImpl(productGateway);

    @Test
    public void executeShouldReturnAllProducts() throws UnavailableDataException, ProductNotFoundException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        1
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldNotProcessProductsWithQuantity() throws UnavailableDataException, ProductNotFoundException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        var unexpectedProduct = new Product(2, "Title Two", "Description Two", 20_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(2, 0)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        1
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
        assertThat(checkoutResult.checkoutItems).noneMatch(ci -> ci.getProduct().getId() == unexpectedProduct.getId());
    }

    @Test
    public void executeShouldSumQuantitiesForDuplicateProducts() throws UnavailableDataException, ProductNotFoundException {
        var expectedProduct = new Product(1, "Title One", "Description One", 10_000L, false);
        given(productGateway.getProductsById(anyCollection())).willReturn(List.of(expectedProduct));
        var cmd = new CheckoutCommand(List.of(
                new CheckoutItemCommand(1, 1),
                new CheckoutItemCommand(1, 2)
        ));

        var expectedCheckout = new Checkout(List.of(
                new CheckoutItem(
                        expectedProduct,
                        3
                )
        ));

        var checkoutResult = useCase.execute(cmd);

        then(productGateway)
                .should(times(1))
                .getProductsById(anyCollection());
        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(expectedCheckout);
    }

    @Test
    public void executeShouldThrowProductNotFoundExceptionWhenOneOfTheProductsDoesNotExist() throws UnavailableDataException {
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
}
