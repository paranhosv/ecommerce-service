package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckout;
import com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities.CheckoutRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities.CheckoutResponse.toCheckoutResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(controllers = CheckoutController.class)
public class CheckoutControllerTests {
    private final ObjectMapper mapper = new ObjectMapper();

    private final EasyRandom generator = new EasyRandom();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MakeCheckout makeCheckout;

    @Test
    public void makeCheckoutShouldReturnAllCheckoutResult() throws Exception {
        var expectedCheckout = generator.nextObject(Checkout.class);
        var checkoutRequest = generator.nextObject(CheckoutRequest.class);

        given(makeCheckout.execute(any(CheckoutCommand.class)))
                .willReturn(expectedCheckout);

        mockMvc.perform(
                        post("/v1/checkout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(toCheckoutResponse(expectedCheckout))));

        then(makeCheckout)
                .should(times(1))
                .execute(any(CheckoutCommand.class));
    }

    @Test
    public void makeCheckoutShouldReturn500WhenDataIsUnavailable() throws Exception {
        var checkoutRequest = generator.nextObject(CheckoutRequest.class);

        given(makeCheckout.execute(any(CheckoutCommand.class)))
                .willThrow(new UnavailableDataException("Data unavailable"));

        mockMvc.perform(
                        post("/v1/checkout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isInternalServerError());

        then(makeCheckout)
                .should(times(1))
                .execute(any(CheckoutCommand.class));
    }

    @Test
    public void makeCheckoutShouldReturn400WhenProductIsNotFound() throws Exception {
        var checkoutRequest = generator.nextObject(CheckoutRequest.class);

        given(makeCheckout.execute(any(CheckoutCommand.class)))
                .willThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(
                        post("/v1/checkout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(checkoutRequest)))
                .andExpect(status().isBadRequest());

        then(makeCheckout)
                .should(times(1))
                .execute(any(CheckoutCommand.class));
    }
}
