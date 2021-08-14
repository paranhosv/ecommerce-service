package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.junit.jupiter.api.Test;

import static com.victorparanhos.ecommerceservice.utils.JsonFileProductGenerator.jsonFileProductsToList;
import static com.victorparanhos.ecommerceservice.utils.ProductGenerator.generateProductList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class GetProductsImplTests {
    private final ProductGateway productsGateway = mock(ProductGateway.class);
    private final GetProductsImpl useCase = new GetProductsImpl(productsGateway);

    @Test
    public void executeShouldReturnAllProducts() throws UnavailableDataException {
        given(productsGateway.getProducts()).willReturn(generateProductList());
        var getProductsResult = useCase.execute();

        then(productsGateway)
                .should(times(1))
                .getProducts();
        assertThat(getProductsResult)
                .usingRecursiveComparison()
                .isEqualTo(jsonFileProductsToList());
    }
}
