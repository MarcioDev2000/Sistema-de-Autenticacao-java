package com.uevocola.com.uevocola;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class UevocolaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UevocolaApplication.class, args);
	}
	
}
