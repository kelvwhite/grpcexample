package com.example.grpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GrpcexampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcexampleApplication.class, args);
	}
}
