package com.sprinklr.JStack.Analyser.utils;

import com.sprinklr.JStack.Analyser.model.SingleThread;
import com.sprinklr.JStack.Analyser.model.Statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatisticsUtil {
    private Statistics statistics;

    public StatisticsUtil(Statistics statistics) {
        this.statistics = statistics;
    }

    public void buildStatistics(HashMap<String, SingleThread> allThreads) {
        statistics.setThreadType(new HashMap<>());
        statistics.setDaemonThreads(new ArrayList<>());
        statistics.setNonDaemonThreads(new ArrayList<>());
        processThreads(allThreads);
    }

    public void processThreads(HashMap<String, SingleThread> allThreads) {
        for (Map.Entry<String, SingleThread> entry : allThreads.entrySet()) {
            String threadId = entry.getKey();
            SingleThread curThread = entry.getValue();
            //Getting state
            String state = curThread.getState();
            if (Objects.equals(state, "")) {
                state = "UNCLASSIFIED";
            }
            statistics.getThreadType().putIfAbsent(state, new ArrayList<>());
            statistics.getThreadType().get(state).add(threadId);
            //Daemon Vs Non Daemon
            if (curThread.isDaemon()) {
                statistics.getDaemonThreads().add(threadId);
            } else {
                statistics.getNonDaemonThreads().add(threadId);
            }
        }
    }
}
