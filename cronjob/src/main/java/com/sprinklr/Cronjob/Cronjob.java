package com.sprinklr.Cronjob;

import com.sprinklr.Cronjob.service.EmailService;
import com.sprinklr.Cronjob.service.SlackService;
import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.service.ThreadDumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class Cronjob {
    @Autowired
    private ThreadDumpService threadDumpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SlackService slackService;
    @Value("${APP_PID}")
    private long pid;
    @Value("${FRONT_END_URL:http://localhost:3000}")
    private String FRONT_END_URL;

    @Scheduled(fixedRate = 5000000)
    public void scheduledProcess() throws IOException, InterruptedException {
        System.out.println("Going to start taking Thread Dumps ");
        String jStackPath = generateJStack();
        System.out.println("Completed taking a full JStack File");
        analyseJStack(jStackPath);
        System.out.println("Analysing complete");
        deleteJStack(jStackPath);
    }

    private void analyseJStack(String jStackPath) throws IOException {
        String content = Files.readString(Paths.get(jStackPath));
        CombinedThreadDump combinedThreadDump = threadDumpService.convertToWorkableFormat(content, "", "check");

        String id = combinedThreadDump.getId();
        //If id!=null then we have crossed the threshold
        if (id != null) {
            takeAction(id);
        }
    }

    private void takeAction(String id) {
        System.out.println("Crossed the threshold, Please see report at :");
        String reportUrl = FRONT_END_URL + "/dbId/" + id + "/singleDumpReport";
        System.out.println(reportUrl);
        slackService.sendMessage(reportUrl);
        emailService.sendmail(reportUrl);
    }

    private String generateJStack() throws IOException, InterruptedException {
        //The path of newly created JStack
        String filePath = "./outputs/JStack_at_" + new Date() + ".txt";
        for (int i = 1; i <= 1; i++) {
            if (i > 1) {
                TimeUnit.SECONDS.sleep(10);
            }
            takeSingleDump(filePath);
            System.out.println("Took a single JStack");
        }
        return filePath;
    }

    private void takeSingleDump(String filePath) throws IOException {
        String command = "jstack -l " + pid;
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));

        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private void deleteJStack(String filePath) {
        new File(filePath).delete();
    }
}
