package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import com.victorparanhos.ecommerceservice.infra.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;

import static java.util.stream.Collectors.toList;

public class JsonProductsFileGateway implements ProductGateway {
    @Autowired
    private ProductRepository repo;

    @Override
    public Collection<Product> getProducts() throws UnavailableDataException {
        return repo.findAll()
                .stream()
                .map(jsonFileProduct -> jsonFileProduct.toProduct(jsonFileProduct.isGift))
                .collect(toList());
    }

    @Override
    public Collection<Product> getProductsById(Collection<Integer> productIds) throws UnavailableDataException {
        var productIdHash = new HashSet<>(productIds);
        return repo.findAll()
                .stream()
                .filter(jsonFileProduct -> productIdHash.contains(jsonFileProduct.id))
                .map(jsonFileProduct -> jsonFileProduct.toProduct(false))
                .collect(toList());
    }

    @Override
    public Collection<Product> getGifts() throws UnavailableDataException {
        return repo.findAll()
                .stream()
                .filter(jsonFileProduct -> jsonFileProduct.isGift)
                .map(jsonFileProduct -> jsonFileProduct.toProduct(true))
                .collect(toList());
    }
}
