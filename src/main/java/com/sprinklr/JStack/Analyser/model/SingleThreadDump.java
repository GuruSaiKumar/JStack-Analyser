package com.sprinklr.JStack.Analyser.model;


import java.util.*;

public class SingleThreadDump {
    private String name;
    private HashMap<String, SingleThread> allThreads;// tid -> singleThread
    private HashMap<Integer, ArrayList<String>> allStackTraces;//hashId -> StackTrace
    private HashMap<Integer, ArrayList<String>> hashIdToThreadIds;//hashId -> list of tid
    private HashMap<String, ArrayList<String>> prefMatching;// prefix -> list of tid
    private HashMap<Integer, String> allMethods;//methodHashId -> method(String)
    private HashMap<Integer, ArrayList<String>> methodHashIdToThreadIds;//methodHashId -> list of tid
    private Set<String> deadLockedThreadNames;
    private ArrayList<String> deadLockedThreadIds;
    private HashMap<String, ArrayList<String>> mapStackTraceLength;// groupByLen -> list of tids.
    private Statistics statistics;

    public SingleThreadDump() {
        this.name = "";
        this.allThreads = new HashMap<>();
        this.allStackTraces = new HashMap<>();
        this.hashIdToThreadIds = new HashMap<>();
        this.prefMatching = new HashMap<>();
        this.allMethods = new HashMap<>();
        this.methodHashIdToThreadIds = new HashMap<>();
        this.deadLockedThreadNames = new HashSet<>();
        this.deadLockedThreadIds = new ArrayList<>();
        this.mapStackTraceLength = new HashMap<>();
        this.statistics = new Statistics();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, SingleThread> getAllThreads() {
        return allThreads;
    }

    public void setAllThreads(HashMap<String, SingleThread> allThreads) {
        this.allThreads = allThreads;
    }

    public HashMap<Integer, ArrayList<String>> getAllStackTraces() {
        return allStackTraces;
    }

    public void setAllStackTraces(HashMap<Integer, ArrayList<String>> allStackTraces) {
        this.allStackTraces = allStackTraces;
    }

    public HashMap<Integer, ArrayList<String>> getHashIdToThreadIds() {
        return hashIdToThreadIds;
    }

    public void setHashIdToThreadIds(HashMap<Integer, ArrayList<String>> hashIdToThreadIds) {
        this.hashIdToThreadIds = hashIdToThreadIds;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public HashMap<String, ArrayList<String>> getPrefMatching() {
        return prefMatching;
    }

    public void setPrefMatching(HashMap<String, ArrayList<String>> prefMatching) {
        this.prefMatching = prefMatching;
    }

    public HashMap<Integer, String> getAllMethods() {
        return allMethods;
    }

    public void setAllMethods(HashMap<Integer, String> allMethods) {
        this.allMethods = allMethods;
    }

    public HashMap<Integer, ArrayList<String>> getMethodHashIdToThreadIds() {
        return methodHashIdToThreadIds;
    }

    public void setMethodHashIdToThreadIds(HashMap<Integer, ArrayList<String>> methodHashIdToThreadIds) {
        this.methodHashIdToThreadIds = methodHashIdToThreadIds;
    }

    public Set<String> getDeadLockedThreadNames() {
        return deadLockedThreadNames;
    }

    public void setDeadLockedThreadNames(Set<String> deadLockedThreadNames) {
        this.deadLockedThreadNames = deadLockedThreadNames;
    }

    public ArrayList<String> getDeadLockedThreadIds() {
        return deadLockedThreadIds;
    }

    public void setDeadLockedThreadIds(ArrayList<String> deadLockedThreadIds) {
        this.deadLockedThreadIds = deadLockedThreadIds;
    }

    public HashMap<String, ArrayList<String>> getMapStackTraceLength() {
        return mapStackTraceLength;
    }

    public void setMapStackTraceLength(HashMap<String, ArrayList<String>> mapStackTraceLength) {
        this.mapStackTraceLength = mapStackTraceLength;
    }
}
