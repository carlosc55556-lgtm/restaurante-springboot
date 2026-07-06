package com.restaurante.restauranteweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.restaurante")
@EnableJpaRepositories(basePackages = "com.restaurante.repository")
@EntityScan(basePackages = "com.restaurante.entity")
public class RestauranteWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestauranteWebApplication.class, args);
    }
}