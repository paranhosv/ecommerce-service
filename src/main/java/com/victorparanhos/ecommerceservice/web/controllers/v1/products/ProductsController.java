package com.victorparanhos.ecommerceservice.web.controllers.v1.products;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.GetProducts;
import com.victorparanhos.ecommerceservice.web.controllers.v1.products.entities.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("v1/products")
public class ProductsController {
    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private GetProducts getProducts;

    @RequestMapping(method = RequestMethod.GET, name = "/",
            produces = "application/json")
    public List<ProductResponse> getProducts() throws UnavailableDataException {
        logger.info("Received getProducts request");
        return getProducts.execute()
                .stream()
                .map(ProductResponse::new)
                .collect(toList());
    }

}
