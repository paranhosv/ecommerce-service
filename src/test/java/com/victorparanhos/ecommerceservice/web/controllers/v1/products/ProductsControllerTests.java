package com.victorparanhos.ecommerceservice.web.controllers.v1.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.GetProducts;
import com.victorparanhos.ecommerceservice.web.controllers.v1.products.entities.ProductResponse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(controllers = ProductsController.class)
public class ProductsControllerTests {
    private final ObjectMapper mapper = new ObjectMapper();

    private final EasyRandom generator = new EasyRandom();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProducts getProducts;

    @Test
    public void getProductsShouldReturnAllProducts() throws Exception {
        var product = generator.nextObject(Product.class);

        given(getProducts.execute())
                .willReturn(List.of(product));

        mockMvc.perform(
                        get("/v1/products")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ProductResponse.toProductsResponse(List.of(product)))));

        then(getProducts)
                .should(times(1))
                .execute();
    }

    @Test
    public void getProductsShouldThrow500WhenDataIsUnavailable() throws Exception {
        given(getProducts.execute())
                .willThrow(new UnavailableDataException("Data unavailable"));

        mockMvc.perform(
                        get("/v1/products")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        then(getProducts)
                .should(times(1))
                .execute();
    }
}
