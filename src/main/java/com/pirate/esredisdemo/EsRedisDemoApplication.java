package com.pirate.esredisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsRedisDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EsRedisDemoApplication.class, args);
		System.out.println("服务开启");
	}
}
