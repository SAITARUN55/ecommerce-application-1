package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// This package contains the main method which runs the application

// the @EnableJpaRepositories annotation, tells Spring that this package contains our data repositories
@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
// The @EntityScan annotation tells Spring that this package contains our data models
@EntityScan("com.example.demo.model.persistence")
// EMG - To implement JWT, the exclusion of the default SecurityAutoConfiguration class is added
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SareetaApplication {

	// EMG - To implement JWT, the BCryptPasswordEncoder is added
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

}
