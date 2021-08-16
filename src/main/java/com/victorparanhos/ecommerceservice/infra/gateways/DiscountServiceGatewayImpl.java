package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.gateways.DiscountServiceGateway;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.hash.discountservice.DiscountGrpc;
import io.hash.discountservice.GetDiscountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscountServiceGatewayImpl implements DiscountServiceGateway {
    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceGatewayImpl.class);

    private final DiscountGrpc.DiscountBlockingStub stub;

    public DiscountServiceGatewayImpl(String serverName, int serverPort) {
        var channel = ManagedChannelBuilder.forAddress(serverName, serverPort)
                .usePlaintext()
                .build();

        stub = DiscountGrpc.newBlockingStub(channel);
    }

    public float getDiscount(int productId) {
        logger.info("Calling discount service to get discount for productId {}", productId);
        var request = GetDiscountRequest.newBuilder()
                .setProductID(productId)
                .build();
        try {
            var discount = stub.getDiscount(request).getPercentage();
            logger.info("Discount for productId {} is {}", productId, discount);
            return discount;
        } catch (StatusRuntimeException e) {
            logger.warn("RPC failed with status {}. Discount will be zero percent for productId {}",
                    e.getStatus().getCode(),
                    productId);
            return 0;
        }
    }

}
