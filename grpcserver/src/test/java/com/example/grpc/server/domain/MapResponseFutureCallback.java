package com.example.grpc.server.domain;

import com.example.grpc.server.MapResponse;
import com.google.common.base.Verify;
import com.google.common.util.concurrent.FutureCallback;
import io.grpc.Status;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.concurrent.CountDownLatch;

public class MapResponseFutureCallback implements FutureCallback<MapResponse> {

    private CountDownLatch latch;

    @Getter
    private MapResponse mapResponse;

    public MapResponseFutureCallback(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onSuccess(@Nullable MapResponse mapResponse) {
        this.mapResponse = mapResponse;
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
