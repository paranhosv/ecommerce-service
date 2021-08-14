package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;

import java.util.Collection;

public class GetProductsImpl implements GetProducts {

    private final ProductGateway productsGateway;

    public GetProductsImpl(ProductGateway productsGateway) {
        this.productsGateway = productsGateway;
    }

    @Override
    public Collection<Product> execute() throws UnavailableDataException {
        return productsGateway.getProducts();
    }
}
