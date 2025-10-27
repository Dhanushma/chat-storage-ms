package com.dd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ChatStorageMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatStorageMsApplication.class, args);
	}

}
