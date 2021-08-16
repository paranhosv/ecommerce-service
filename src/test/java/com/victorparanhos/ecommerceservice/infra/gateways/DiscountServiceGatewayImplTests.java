package com.victorparanhos.ecommerceservice.infra.gateways;

import com.victorparanhos.ecommerceservice.applicationcore.domain.exceptions.DiscountServerException;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import io.hash.discountservice.DiscountGrpc;
import io.hash.discountservice.GetDiscountRequest;
import io.hash.discountservice.GetDiscountResponse;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

public class DiscountServiceGatewayImplTests {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final DiscountServiceGatewayImpl gateway = new DiscountServiceGatewayImpl("test", 0);

    @Test
    public void getDiscountShouldReturnDiscountPercentage() throws NoSuchFieldException, IllegalAccessException,
            IOException, DiscountServerException {
        var stub = setUpMockStub();
        setStub(stub);

        var response = gateway.getDiscount(1);
        assertEquals(0.127F, response);
    }

    @Test
    public void getDiscountShouldDiscountServerExceptionOnError() throws NoSuchFieldException, IllegalAccessException,
            IOException {
        var stubError = setUpMockErrorStub();
        setStub(stubError);

        assertThatThrownBy(() ->
                gateway.getDiscount(1)
        ).isInstanceOf(DiscountServerException.class);
    }

    private DiscountGrpc.DiscountBlockingStub setUpMockStub() throws IOException {
        var serviceImpl =
                mock(DiscountGrpc.DiscountImplBase.class, delegatesTo(
                        new DiscountGrpc.DiscountImplBase() {
                            @Override
                            public void getDiscount(GetDiscountRequest request, StreamObserver<GetDiscountResponse> respObserver) {
                                respObserver.onNext(GetDiscountResponse.newBuilder().setPercentage(0.127F).build());
                                respObserver.onCompleted();
                            }
                        }));

        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(serviceImpl).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        // Create a DiscountBlockingStub using the in-process channel;
        return DiscountGrpc.newBlockingStub(channel);
    }

    private DiscountGrpc.DiscountBlockingStub setUpMockErrorStub() throws IOException {
        var serviceErrorImpl =
                mock(DiscountGrpc.DiscountImplBase.class, delegatesTo(
                        new DiscountGrpc.DiscountImplBase() {
                            // By default, the client will receive Status.UNIMPLEMENTED for all RPCs.
                        }));

        // Generate a unique in-process server name.
        String serverNameError = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverNameError).directExecutor().addService(serviceErrorImpl).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        ManagedChannel channelError = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverNameError).directExecutor().build());

        // Create a DiscountBlockingStub using the in-process channel;
        return DiscountGrpc.newBlockingStub(channelError);
    }

    private void setStub(DiscountGrpc.DiscountBlockingStub stub) throws NoSuchFieldException, IllegalAccessException {
        Field field = gateway.getClass().getDeclaredField("stub");
        field.setAccessible(true);
        field.set(gateway, stub);
    }
}
