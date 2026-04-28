package com.derekgelvez.lawfirmweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.derekgelvez")
@EnableJpaRepositories(basePackages = "com.derekgelvez")
@EntityScan(basePackages = "com.derekgelvez")
public class LawFirmWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LawFirmWebApplication.class, args);
    }
}
