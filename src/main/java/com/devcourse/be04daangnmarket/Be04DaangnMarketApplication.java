package com.devcourse.be04daangnmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Be04DaangnMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(Be04DaangnMarketApplication.class, args);
	}

}
