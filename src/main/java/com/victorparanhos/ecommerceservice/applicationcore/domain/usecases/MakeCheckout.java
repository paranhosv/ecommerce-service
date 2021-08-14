package com.victorparanhos.ecommerceservice.applicationcore.domain.usecases;

import com.victorparanhos.ecommerceservice.applicationcore.domain.commands.CheckoutCommand;
import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;

public interface MakeCheckout {

    Checkout execute(CheckoutCommand checkoutCmd) throws UnavailableDataException, ProductNotFoundException;
}
