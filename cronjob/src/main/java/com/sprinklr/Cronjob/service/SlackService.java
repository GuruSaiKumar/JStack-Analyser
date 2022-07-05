package com.sprinklr.Cronjob.service;

public interface SlackService {
    void sendMessage(String reportUrl);
}
