package com.victorparanhos.ecommerceservice.applicationcore.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.DiscountServerException;

public interface DiscountServiceGateway {
    float getDiscount(int productId) throws DiscountServerException;
}
