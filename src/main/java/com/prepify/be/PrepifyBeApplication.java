package com.prepify.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
//@EnableJpaRepositories("com.prepify.be.repositories")
//@EntityScan("com.prepify.be.entities")
public class PrepifyBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrepifyBeApplication.class, args);
	}

}
