package com.example.HM_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HmFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmFrontendApplication.class, args);
	}

}
