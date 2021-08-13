package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;

import java.util.List;

public class GetProductsImpl implements GetProducts {

    private final ProductGateway productsGateway;

    public GetProductsImpl(ProductGateway productsGateway) {
        this.productsGateway = productsGateway;
    }

    @Override
    public List<Product> execute() throws UnavailableDataException {
        return productsGateway.getProducts();
    }
}
