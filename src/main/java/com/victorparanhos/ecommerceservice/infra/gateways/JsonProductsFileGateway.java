package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.gateways.ProductGateway;
import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;
import com.victorparanhos.ecommerceservice.infra.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class JsonProductsFileGateway implements ProductGateway {
    @Autowired
    private ProductRepository repo;

    @Override
    public List<Product> getProducts() throws UnavailableDataException {
        return repo.findAll()
                .stream()
                .map(JsonFileProduct::toProduct)
                .collect(toList());
    }
}
