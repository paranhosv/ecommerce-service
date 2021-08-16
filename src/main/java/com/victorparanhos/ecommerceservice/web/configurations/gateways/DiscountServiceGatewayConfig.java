package com.victorparanhos.ecommerceservice.web.configurations.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import com.victorparanhos.ecommerceservice.infra.gateways.DiscountServiceGatewayImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discountservice")
public class DiscountServiceGatewayConfig {
    private String hostname;
    private int port;

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Bean
    public DiscountServiceGateway createDiscountServiceGatewayImpl() {
        return new DiscountServiceGatewayImpl(hostname, port);
    }
}
