package com.gad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class GadApplication {

	public static void main(String[] args) {
		SpringApplication.run(GadApplication.class, args);

	}
}
