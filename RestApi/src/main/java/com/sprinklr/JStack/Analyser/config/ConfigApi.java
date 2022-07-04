package com.sprinklr.JStack.Analyser.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//This configuration is necessary for our beans to be accessible to Cronjob application
//Property source is required for Cronjob to access the database configuration.
@Configuration
@ComponentScan(basePackages = {"com.sprinklr.JStack.Analyser"})
@PropertySource(value = "database.properties")
public class ConfigApi {
}
