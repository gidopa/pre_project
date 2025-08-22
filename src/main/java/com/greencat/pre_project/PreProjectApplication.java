package com.greencat.pre_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
public class PreProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreProjectApplication.class, args);
	}

}
