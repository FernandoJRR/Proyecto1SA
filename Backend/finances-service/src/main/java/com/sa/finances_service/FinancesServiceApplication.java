package com.sa.finances_service;

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
@ComponentScan(basePackages = {"com.sa.finances_service","com.sa.infrastructure.jwtadapter"})
@EnableConfigurationProperties(AppProperties.class)
public class FinancesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancesServiceApplication.class, args);
	}

}
