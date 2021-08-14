package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;
import com.victorparanhos.ecommerceservice.infra.repositories.ProductRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
public class JsonProductsFileGatewayTests {
    private final EasyRandom generator = new EasyRandom();

    @Autowired
    private JsonProductsFileGateway gateway;

    @MockBean
    private ProductRepository repo;

    @Test
    public void getProductsShouldReturnAllProducts() throws UnavailableDataException {
        var jsonFileProducts = generator.objects(JsonFileProduct.class, 5).collect(toList());
        given(repo.findAll()).willReturn(jsonFileProducts);
        var getProductsResult = gateway.getProducts();

        then(repo)
                .should(times(1))
                .findAll();
        assertThat(getProductsResult)
                .usingRecursiveComparison()
                .isEqualTo(jsonFileProducts);
    }

    @Test
    public void getProductsByIdShouldReturnProducts() throws UnavailableDataException {
        var jsonFileProducts = generator.objects(JsonFileProduct.class, 5).collect(toList());
        var randomProduct = jsonFileProducts.get(2);
        given(repo.findAll()).willReturn(jsonFileProducts);
        var getProductsResult = gateway.getProductsById(List.of(randomProduct.id));

        then(repo)
                .should(times(1))
                .findAll();
        assertThat(getProductsResult)
                .usingRecursiveComparison()
                .isEqualTo(List.of(randomProduct.toProduct()));
    }
}
