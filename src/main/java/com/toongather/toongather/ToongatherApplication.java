package com.toongather.toongather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class ToongatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToongatherApplication.class, args);
	}

}
