package com.countries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableAspectJAutoProxy
public class CountriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountriesApplication.class, args);
	}

}
