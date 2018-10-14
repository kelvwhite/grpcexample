package com.example.grpc.client.services;

import com.example.grpc.client.config.ClientConfiguration;
import com.example.grpc.server.Greeting;
import com.example.grpc.server.GrpcExampleServiceGrpc;
import com.example.grpc.server.Person;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class GrpcClientService {
    private final ClientConfiguration clientConfiguration;

    private GrpcExampleServiceGrpc.GrpcExampleServiceBlockingStub grpcExampleBlockingStub;

    private GrpcExampleServiceGrpc.GrpcExampleServiceFutureStub grpcExampleAsyncStub;

    public GrpcClientService(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(
                        clientConfiguration.getHost(),
                        clientConfiguration.getPort()).usePlaintext().build();

        grpcExampleBlockingStub =
                GrpcExampleServiceGrpc.newBlockingStub(managedChannel);

        grpcExampleAsyncStub =
                GrpcExampleServiceGrpc.newFutureStub(managedChannel);
    }

    @Scheduled(initialDelay = 2000L, fixedDelay = 3000L)
    public void sayHelloScheduled() {
        sayHello("Client", "Service");
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

}
