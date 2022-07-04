package com.sprinklr.Cronjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class CronjobApplication {
    public static void main(String[] args) {
        SpringApplication.run(CronjobApplication.class, args);
    }
}
