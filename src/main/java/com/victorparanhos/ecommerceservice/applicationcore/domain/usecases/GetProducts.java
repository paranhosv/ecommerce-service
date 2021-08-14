package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Product;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;

import java.util.Collection;

public interface GetProducts {

    Collection<Product> execute() throws UnavailableDataException;
}
