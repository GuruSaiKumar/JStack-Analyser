package com.sprinklr.JStack.Analyser.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {
    private HashMap<String, ArrayList<String>> threadType;
    private ArrayList<String> daemonThreads;//list of tid
    private ArrayList<String> nonDaemonThreads;//list of tid

    public Statistics() {
        this.threadType = new HashMap<>();
        this.daemonThreads = new ArrayList<>();
        this.nonDaemonThreads = new ArrayList<>();
    }

    public HashMap<String, ArrayList<String>> getThreadType() {
        return threadType;
    }

    public void setThreadType(HashMap<String, ArrayList<String>> threadType) {
        this.threadType = threadType;
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
