package com.sprinklr.JStack.Analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JStackAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(JStackAnalyserApplication.class, args);
	}

}
