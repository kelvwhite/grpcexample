package com.example.grpc.client;

import com.example.grpc.client.domain.GreetingFutureCallback;
import com.example.grpc.client.components.GrpcExampleClient;
import com.example.grpc.server.Greeting;
import com.example.grpc.server.MapResponse;
import io.grpc.StatusRuntimeException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrpcexampleApplicationTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private GrpcExampleClient grpcClient;

	@Test
	public void contextLoads() {
	}

	@Test
	public void sayHello() {
		assertThat(grpcClient.sayHello("Kelvin", "White")).isEqualTo("Welcome Kelvin White");
	}

	@Test
	public void sayHelloError() {
		exception.expect(StatusRuntimeException.class);
		exception.expectMessage("Bad name");
		grpcClient.sayHello("Throw", "Error");
	}

	@Test
	public void sayHelloAsync() throws ExecutionException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		GreetingFutureCallback greetingFutureCallback = new GreetingFutureCallback(latch);
		grpcClient.sayHelloAsync("Kelvin", "White", greetingFutureCallback);

		latch.await(1, TimeUnit.SECONDS);

		Greeting greeting = (Greeting)greetingFutureCallback.getGreeting();
		assertThat(greeting.getMessage()).isEqualTo("Welcome Kelvin White");
	}

	@Test
	public void sayHelloAsyncMultipleRequests() throws ExecutionException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(6);

		GreetingFutureCallback greetingFutureCallback = new GreetingFutureCallback(latch);
		grpcClient.sayHelloAsync("Kelvin", "White", greetingFutureCallback);
		grpcClient.sayHelloAsync("Lisa", "White", greetingFutureCallback);
		grpcClient.sayHelloAsync("Sophie", "White", greetingFutureCallback);
		grpcClient.sayHelloAsync("Daniel", "White", greetingFutureCallback);
		grpcClient.sayHelloAsync("Button", "White", greetingFutureCallback);
		grpcClient.sayHelloAsync("Daisy", "White", greetingFutureCallback);

		latch.await(1, TimeUnit.SECONDS);

		assertThat(greetingFutureCallback.isRecieved()).isTrue();
	}

	@Test
	public void getMap() {
		MapResponse mapResponse = grpcClient.getMap();
		Map<String, Integer> map = mapResponse.getMappedValuesMap();
		assertThat(map.size()).isEqualTo(2);
		assertThat(map.get("one")).isEqualTo(1);
		assertThat(map.get("two")).isEqualTo(2);
	}
}
