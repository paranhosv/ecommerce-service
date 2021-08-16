package com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions;

public class EmptyBasketException extends Exception {
    public EmptyBasketException(String errorMessage) {
        super(errorMessage);
    }
}
