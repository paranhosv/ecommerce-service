package com.victorparanhos.ecommerceservice.infra.repositories;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.infra.entities.JsonFileProduct;

import java.util.Collection;

public interface ProductRepository {

    Collection<JsonFileProduct> findAll() throws UnavailableDataException;
}
