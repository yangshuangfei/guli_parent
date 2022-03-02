package com.stitch.infrastructure.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

@EnableDiscoveryClient
public class InfrastructureApigatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApigatewayApplication.class, args);
    }
}
