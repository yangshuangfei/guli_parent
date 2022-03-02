package com.stitch.service.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.stitch.service"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.stitch.service.trade.feign")
public class ServiceTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTradeApplication.class, args);
    }
}
