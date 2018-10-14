package com.example.grpc.client.domain;

import com.google.common.base.Verify;
import com.google.common.util.concurrent.FutureCallback;
import io.grpc.Status;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.concurrent.CountDownLatch;

public class GreetingFutureCallback<Greeting> implements FutureCallback<Greeting> {

    private final CountDownLatch latch;

    @Getter
    private Greeting greeting;

    @Getter
    private boolean recieved;

    public GreetingFutureCallback(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onSuccess(@Nullable Greeting greeting) {
        this.greeting = greeting;
        recieved = true;
        latch.countDown();
    }

    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();

        Status status = Status.fromThrowable(throwable);
        Verify.verify(status.getCode() == Status.Code.INTERNAL);
        latch.countDown();
    }
}