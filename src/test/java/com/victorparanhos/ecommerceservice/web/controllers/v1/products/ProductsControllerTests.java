package com.victorparanhos.ecommerceservice.web.controllers.v1.products;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.GetProducts;
import com.victorparanhos.ecommerceservice.web.controllers.v1.products.entities.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureJsonTesters
@WebMvcTest(controllers = ProductsController.class)
public class ProductsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<List<ProductResponse>> jsonProducts;

    @MockBean
    private GetProducts getProducts;

    @Test
    public void getProductsShouldReturnAllProducts() throws Exception {
        Product mockedProduct = new Product(
                1,
                "Title",
                "Description",
                1_000L,
                false);
        given(getProducts.execute())
                .willReturn(List.of(mockedProduct));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/v1/products")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        then(getProducts)
                .should(times(1))
                .execute();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonProducts.write(List.of(new ProductResponse(mockedProduct))).getJson());
    }
}
