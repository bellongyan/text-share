package com.textshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TextShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextShareApplication.class, args);
    }
}
