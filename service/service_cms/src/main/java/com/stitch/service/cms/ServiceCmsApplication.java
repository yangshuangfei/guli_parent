package com.stitch.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.stitch.service"})
@EnableDiscoveryClient
@EnableFeignClients()
public class ServiceCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class, args);
    }
}
