package com.stitch.service.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.stitch"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class ServicestatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicestatisticsApplication.class, args);
    }
}
