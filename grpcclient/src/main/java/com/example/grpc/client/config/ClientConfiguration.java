package com.example.grpc.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "grpcsclient")
public class ClientConfiguration {
    private String host;
    private int port;
}
