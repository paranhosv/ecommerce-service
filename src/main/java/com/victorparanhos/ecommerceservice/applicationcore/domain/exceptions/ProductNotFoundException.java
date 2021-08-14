package com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
