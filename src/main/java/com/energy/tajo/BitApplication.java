package com.energy.tajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.energy.tajo", "com.energy.tajo.config"})
@PropertySource("classpath:config/application-twilio.yml")
@EnableJpaAuditing
public class BitApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitApplication.class, args);
    }
}