package com.sprinklr.JStack.Analyser;
import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.service.ThreadDumpService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final long pid = ProcessHandle.current().pid();
    private final String FRONT_END_URL = "http://localhost:3000";

    @Scheduled(fixedRate = 5000000)
    public void scheduledProcess() throws IOException, InterruptedException {
        System.out.println("Going to start taking taking ");
        String jStackPath = generateJStack();
        System.out.println("Completed taking a full JStack File");
        analyseJStack(jStackPath);
        System.out.println("Processing complete");
    }
    private void analyseJStack(String jStackPath) throws IOException {
        String content = Files.readString(Paths.get(jStackPath));
        CombinedThreadDump combinedThreadDump = threadDumpService.convertToWorkableFormat(content, "", "check");

        String id = combinedThreadDump.getId();
        //If id!=null then we have crossed the threshold
        if(id!=null){
            takeAction(id);
        }
    }

    private void takeAction(String id) {
        System.out.println("crossed the threshold, Please see report at :");
        String reportUrl = FRONT_END_URL + "/dbId/" + id + "/singleDumpReport";
        System.out.println(reportUrl);
    }

    private String generateJStack() throws IOException, InterruptedException {
        //The path of newly created JStack
        String filePath = "./outputs/JStack_at_" + new Date() + ".txt";
        for (int i = 1; i <= 5; i++) {
            if(i>1){
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
}
