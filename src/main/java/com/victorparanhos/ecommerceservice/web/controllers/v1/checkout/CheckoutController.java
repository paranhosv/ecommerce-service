package com.victorparanhos.ecommerceservice.web.controllers.v1.checkout;

import com.victorparanhos.ecommerceservice.applicationcore.domain.entities.Checkout;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.ProductNotFoundException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.UnavailableDataException;
import com.victorparanhos.ecommerceservice.applicationcore.domain.usecases.MakeCheckout;
import com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities.CheckoutRequest;
import com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities.CheckoutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.victorparanhos.ecommerceservice.web.controllers.v1.checkout.entities.CheckoutResponse.toCheckoutResponse;

@RestController
@RequestMapping("v1/checkout")
public class CheckoutController {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private MakeCheckout makeCheckout;

    @RequestMapping(method = RequestMethod.POST, name = "/",
            produces = "application/json")
    public CheckoutResponse makeCheckout(
            @RequestBody CheckoutRequest req
    ) throws UnavailableDataException, ProductNotFoundException {
        logger.info("Received checkout request");
        Checkout checkout = makeCheckout.execute(req.toCheckoutCommand());
        return toCheckoutResponse(checkout);
    }
}
