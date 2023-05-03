package com.example.dicoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DicoveryserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DicoveryserviceApplication.class, args);
	}

}
