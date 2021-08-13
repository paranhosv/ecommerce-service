package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.victorparanhos.ecommerceservice.utils.JsonFileProductGenerator.jsonFileProductsToList;
import static com.victorparanhos.ecommerceservice.utils.ProductGenerator.generateProductList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
public class GetProductsImplTests {
    @Autowired
    private GetProductsImpl useCase;

    @MockBean
    private ProductGateway productsGateway;

    @Test
    public void getProductsShouldReturnAllProducts() throws UnavailableDataException {
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
