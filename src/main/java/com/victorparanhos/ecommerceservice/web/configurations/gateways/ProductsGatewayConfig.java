package com.victorparanhos.ecommerceservice.web.configurations.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import com.victorparanhos.ecommerceservice.infra.gateways.JsonProductsFileGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductsGatewayConfig {

    @Bean
    public ProductGateway createProductGatewayImpl() {
        return new JsonProductsFileGateway();
    }
}
