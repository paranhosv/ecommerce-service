package com.victorparanhos.ecommerceservice.web.configurations.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckoutImpl;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckoutConfig {
    @Autowired
    private ProductGateway productsGateway;

    @Autowired
    private DiscountServiceGateway discountServiceGateway;

    @Bean
    public MakeCheckout createMakeCheckoutImpl() {
        return new MakeCheckoutImpl(productsGateway, discountServiceGateway);
    }
}
