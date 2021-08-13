package com.victorparanhos.ecommerceservice.applicationcore.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;

import java.util.List;

public interface ProductGateway {

    List<Product> getProducts() throws UnavailableDataException;
}
