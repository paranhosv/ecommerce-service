package com.victorparanhos.ecommerceservice.applicationcore.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;

import java.util.Collection;

public interface ProductGateway {
    Collection<Product> getProducts() throws UnavailableDataException;
    Collection<Product> getProductsById(Collection<Long> productIds) throws UnavailableDataException;
}
