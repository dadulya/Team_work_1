package com.star.bank_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankProductsApplication.class, args);
	}

}
