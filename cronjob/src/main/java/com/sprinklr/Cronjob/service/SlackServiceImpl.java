package com.sprinklr.Cronjob.service;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class SlackServiceImpl implements SlackService{
    private String webHookUrl = "https://hooks.slack.com/services/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";
    private String oAuthToken = "xoxb-3741979850231-3756583115138-ywSmCtPTDZEzNNkdU3nafS1Y";
    private String slackChannel = "jstack-alerts";
    @Override
    public void sendMessage(String reportUrl) {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append(reportUrl);

        Payload payload = Payload.builder().channel(slackChannel).text(msgBuilder.toString()).build();

        try {
            WebhookResponse webhookResponse = Slack.getInstance().send(webHookUrl,payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
