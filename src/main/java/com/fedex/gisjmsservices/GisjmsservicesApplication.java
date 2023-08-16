package com.fedex.gisjmsservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fedex.gisjmsservices.service.GisJmsService;

@SpringBootApplication
public class GisjmsservicesApplication implements CommandLineRunner {

	@Autowired
	private GisJmsService jmsService;

	public static void main(String[] args) {
		SpringApplication.run(GisjmsservicesApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		jmsService.run();
	}
}
