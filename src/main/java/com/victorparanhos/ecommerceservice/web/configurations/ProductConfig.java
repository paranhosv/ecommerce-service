package com.victorparanhos.ecommerceservice.web.configurations;

import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.GetProductsImpl;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {
    @Autowired
    private ProductGateway productsGateway;

    @Bean
    public GetProductsImpl createGetProductsImpl() {
        return new GetProductsImpl(productsGateway);
    }
}
