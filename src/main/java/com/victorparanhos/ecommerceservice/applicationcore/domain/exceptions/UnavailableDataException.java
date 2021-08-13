package com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions;

public class UnavailableDataException extends Exception {
    public UnavailableDataException(String errorMessage) {
        super(errorMessage);
    }
}
