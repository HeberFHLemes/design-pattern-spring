package com.dio.design_patterns_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * Projeto Spring Boot gerado via Spring Initializr, 
 * com os módulos:
 * 
 * - Spring Data JPA
 * - Spring Web
 * - H2 Database
 * - OpenFeign
 */
@EnableFeignClients
@SpringBootApplication
public class DesignPatternsSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignPatternsSpringApplication.class, args);
	}

}
