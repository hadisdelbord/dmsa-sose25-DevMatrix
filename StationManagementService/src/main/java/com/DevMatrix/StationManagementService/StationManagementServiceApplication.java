package com.DevMatrix.StationManagementService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StationManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StationManagementServiceApplication.class, args);
	}

}
