package com.zubayer.zpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZposClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZposClientApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
