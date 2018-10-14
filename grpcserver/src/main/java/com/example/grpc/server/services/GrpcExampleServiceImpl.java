package com.example.grpc.server.services;

import com.example.grpc.server.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService
public class GrpcExampleServiceImpl extends GrpcExampleServiceGrpc.GrpcExampleServiceImplBase {

    @Override
    public void sayHello(Person request, StreamObserver<Greeting> responseObserver) {
        log.info("Server received (Thread id: {}): {}", Thread.currentThread().getId(), request);

        String message = String.format("Welcome %s %s", request.getFirstName(), request.getLastName());

        Greeting greeting = Greeting.newBuilder().setMessage(message).build();

        log.info("Server responded {}", greeting);

        if (request.getFirstName().equals("Throw") && request.getLastName().equals("Error")) {
            responseObserver.onError(Status.INTERNAL.withDescription("Bad name").asRuntimeException());
        }

        responseObserver.onNext(greeting);
        responseObserver.onCompleted();
    }

    @Override
    public void getMap(MapRequest request, StreamObserver<MapResponse> responseObserver) {
        log.info("Service getMap() (Thread id: {})", Thread.currentThread().getId());

        MapResponse mapResponse = MapResponse.newBuilder().putMappedValues("one", 1).putMappedValues("two", 2).build();

        log.info("Server Response {}", mapResponse);

        responseObserver.onNext(mapResponse);
        responseObserver.onCompleted();
    }
}
