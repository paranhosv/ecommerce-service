package com.victorparanhos.ecommerceservice.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.victorparanhos.ecommerceservice.web.controllers.v1"))
                .paths(PathSelectors.any())
                .build()
                .groupName("v1")
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket apiV2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.victorparanhos.ecommerceservice.web.controllers.v2"))
                .paths(PathSelectors.any())
                .build()
                .groupName("v2")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ecommerce Service REST API")
                .description("Demo service built with Java 11 for an ecommerce domain context")
                .version("1.0.0")
                .build();
    }
}
