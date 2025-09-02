package com.sa.establishment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.sa.shared.handlers.GlobalExceptionHandler;
import com.sa.shared.security.AppProperties;
import com.sa.shared.security.SecurityConfig;
import com.sa.shared.swagger.SwaggerConfig;

@SpringBootApplication
@EnableJpaAuditing
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class, SecurityConfig.class })
@EnableConfigurationProperties(AppProperties.class)
public class EstablishmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstablishmentServiceApplication.class, args);
	}

}
