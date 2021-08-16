package com.victorparanhos.ecommerceservice.infra.gateways;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import io.hash.discountservice.DiscountGrpc;
import io.hash.discountservice.GetDiscountRequest;
import io.hash.discountservice.GetDiscountResponse;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiscountServiceGatewayImplTests {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private DiscountGrpc.DiscountImplBase serviceImpl =
            mock(DiscountGrpc.DiscountImplBase.class, delegatesTo(
                    new DiscountGrpc.DiscountImplBase() {
                         @Override
                         public void getDiscount(GetDiscountRequest request, StreamObserver<GetDiscountResponse> respObserver) {
                           respObserver.onNext(GetDiscountResponse.newBuilder().setPercentage(0.127F).build());
                           respObserver.onCompleted();
                         }
                    }));

    private DiscountGrpc.DiscountImplBase serviceErrorImpl =
            mock(DiscountGrpc.DiscountImplBase.class, delegatesTo(
                    new DiscountGrpc.DiscountImplBase() {
                        // By default the client will receive Status.UNIMPLEMENTED for all RPCs.
                    }));

    private DiscountGrpc.DiscountBlockingStub stub;
    private DiscountGrpc.DiscountBlockingStub stubError;

    private final DiscountServiceGatewayImpl gateway = new DiscountServiceGatewayImpl("test", 0);

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        // Create a HelloWorldClient using the in-process channel;
        stub = DiscountGrpc.newBlockingStub(channel);




        // Generate a unique in-process server name.
        String serverNameError = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverNameError).directExecutor().addService(serviceErrorImpl).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        ManagedChannel channelError = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        // Create a HelloWorldClient using the in-process channel;
        stubError = DiscountGrpc.newBlockingStub(channelError);
    }

    @Test
    public void getDiscountShouldReturnDiscountPercentage() {
        ArgumentCaptor<GetDiscountRequest> requestCaptor = ArgumentCaptor.forClass(GetDiscountRequest.class);

        var response = stub.getDiscount(GetDiscountRequest.newBuilder().setProductID(1).build());

        verify(serviceImpl)
                .getDiscount(requestCaptor.capture(), ArgumentMatchers.any());
        assertEquals(0.127F, response.getPercentage());
    }

    
}
