package com.derekgelvez.lawfirmweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.derekgelvez")
public class LawFirmWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawFirmWebApplication.class, args);
    }

}
