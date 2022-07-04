package com.sprinklr.JStack.Analyser.model;

import java.util.ArrayList;

public class SingleThread {
    String name;
    String state;
    boolean isDaemon;
    int priority;
    int os_priority;
    String tid;
    String nid;
    ArrayList<String> stackTrace;
    String method;//The method at which the thread is running
    int hashId;//This will be set in SingleThreadDump


    //Default constructor is required for bean to initialise
    public SingleThread() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isDaemon() {
        return isDaemon;
    }

    public void setDaemon(boolean daemon) {
        isDaemon = daemon;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOs_priority() {
        return os_priority;
    }

    public void setOs_priority(int os_priority) {
        this.os_priority = os_priority;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<String> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(ArrayList<String> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public int getHashId() {
        return hashId;
    }

    public void setHashId(int hashId) {
        this.hashId = hashId;
    }
}
