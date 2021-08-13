package com.victorparanhos.ecommerceservice.infra.repositories;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;

import java.util.List;

public interface ProductRepository {

    List<JsonFileProduct> findAll() throws UnavailableDataException;
}
