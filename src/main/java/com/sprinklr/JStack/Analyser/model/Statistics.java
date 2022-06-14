package com.sprinklr.JStack.Analyser.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Statistics {
    private final HashMap<String, ArrayList<String>> threadType;
    private  ArrayList<String> daemonThreads;//list of tid
    private  ArrayList<String> nonDaemonThreads;//list of tid

    Statistics(){
        this.threadType = new HashMap<>();
        this.daemonThreads = new ArrayList<>();
        this.nonDaemonThreads = new ArrayList<>();
    }
    Statistics(HashMap<String,SingleThread> allThreads){
        this.threadType = new HashMap<>();
        this.daemonThreads = new ArrayList<>();
        this.nonDaemonThreads = new ArrayList<>();
        processThreads(allThreads);
    }

    public void processThreads(HashMap<String,SingleThread> allThreads){
            for(Map.Entry<String,SingleThread> entry : allThreads.entrySet()){
                String threadId = entry.getKey();
                SingleThread curThread = entry.getValue();
                //Getting state
                String state = curThread.getState();
                if(Objects.equals(state, "")){
                    state = "UNCLASSIFIED";
                }
                this.threadType.putIfAbsent(state, new ArrayList<>());
                this.threadType.get(state).add(threadId);
                //Daemon Vs Non Daemon
                if(curThread.isDaemon()){
                    daemonThreads.add(threadId);
                }
                else{
                    nonDaemonThreads.add(threadId);
                }
            }
    }

    public HashMap<String, ArrayList<String>> getThreadType() {
        return threadType;
    }

    public ArrayList<String> getDaemonThreads() {
        return daemonThreads;
    }

    public void setDaemonThreads(ArrayList<String> daemonThreads) {
        this.daemonThreads = daemonThreads;
    }

    public ArrayList<String> getNonDaemonThreads() {
        return nonDaemonThreads;
    }

    public void setNonDaemonThreads(ArrayList<String> nonDaemonThreads) {
        this.nonDaemonThreads = nonDaemonThreads;
    }
}
