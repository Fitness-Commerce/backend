package com.fitnesscommerce;

import com.fitnesscommerce.global.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class FitnessCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessCommerceApplication.class, args);
	}

}
