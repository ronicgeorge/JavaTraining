package com.gms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class GMApplication {
    public static void main(String... args) {
        SpringApplication.run(GMApplication.class, args);
    }
}
