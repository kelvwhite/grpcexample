package com.example.grpc.client.components;

import com.example.grpc.server.Greeting;
import com.example.grpc.server.GrpcExampleServiceGrpc;
import com.example.grpc.server.MapResponse;
import com.example.grpc.server.Person;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class GrpcExampleClient {

    private GrpcExampleServiceGrpc.GrpcExampleServiceBlockingStub grpcExampleBlockingStub;

    private GrpcExampleServiceGrpc.GrpcExampleServiceFutureStub grpcExampleAsyncStub;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565).usePlaintext().build();

        grpcExampleBlockingStub =
                GrpcExampleServiceGrpc.newBlockingStub(managedChannel);

        grpcExampleAsyncStub =
                GrpcExampleServiceGrpc.newFutureStub(managedChannel);
    }

    public String sayHello(String firstName, String lastName) {
        Person person = Person.newBuilder().setFirstName(firstName)
                .setLastName(lastName).build();
        log.info("client sending {}", person);

        Greeting greeting =
                grpcExampleBlockingStub.sayHello(person);
        log.info("client received {}", greeting);

        return greeting.getMessage();
    }

    public void sayHelloAsync(String firstName, String lastName, FutureCallback<Greeting> futureCallback) {
        Person person = Person.newBuilder().setFirstName(firstName)
                .setLastName(lastName).build();
        log.info("client sending {}", person);

        ListenableFuture<Greeting> response = grpcExampleAsyncStub.sayHello(person);

        Futures.addCallback(response, futureCallback);
    }

    public MapResponse getMap() {
        log.info("client sending getMap()");

        return grpcExampleBlockingStub.getMap(null);
    }
}
