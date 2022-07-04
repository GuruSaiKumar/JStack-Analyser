package com.sprinklr.Cronjob.config;

import com.sprinklr.JStack.Analyser.config.ConfigApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//This is required to import the beans from Our API
@Configuration
@Import(value = {ConfigApi.class})
public class ConfigCronjob {
}
