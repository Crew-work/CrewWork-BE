package com.crewwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CrewworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrewworkApplication.class, args);
	}

}
