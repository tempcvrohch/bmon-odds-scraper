package com.rohanch.bmonoddsscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BmonOddsScraperApplication {
	public static void main(String[] args) {
		SpringApplication.run(BmonOddsScraperApplication.class, args);
	}
}
