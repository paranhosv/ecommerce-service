package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import com.victorparanhos.ecommerceservice.infra.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.victorparanhos.ecommerceservice.utils.JsonFileProductGenerator.jsonFileProductsToList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
public class JsonProductsFileGatewayTests {

    @Autowired
    private ProductGateway gateway;

    @MockBean
    private ProductRepository repo;

    @Test
    public void getProductsShouldReturnAllProducts() throws UnavailableDataException {
        given(repo.findAll()).willReturn(jsonFileProductsToList());
        var getProductsResult = gateway.getProducts();

        then(repo)
                .should(times(1))
                .findAll();
        assertThat(getProductsResult)
                .usingRecursiveComparison()
                .isEqualTo(jsonFileProductsToList());
    }
}
