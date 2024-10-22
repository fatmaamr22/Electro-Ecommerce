package com.example.electro;

import com.example.electro.model.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashMap;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.example.electro.service", "com.example.electro.repository"}) // Only scan service and repository

public class ElectroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectroApplication.class, args);
	}

}
