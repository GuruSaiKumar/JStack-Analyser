package com.sprinklr.JStack.Analyser.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;

@Document(collection = "CombinedThreadDumpData")
public class CombinedThreadDump {
    private ArrayList<SingleThreadDump> lisOfSingleThreadDump;
    private HashMap<Integer, ArrayList<String>> infiniteLoopingThreads; // (stack trace)hashId -> list of tid
    private HashMap<Integer, ArrayList<String>> commonWaitingThreads;// (stack trace)hashId -> list of tid
    private HashMap<Integer, ArrayList<String>> commonBlockedThreads;// (stack trace)hashId -> list of tid
    private HashMap<Integer, ArrayList<String>> commonTimedWaitingThreads;// (stack trace)hashId -> list of tid

    public CombinedThreadDump() {
        this.lisOfSingleThreadDump = new ArrayList<>();
        this.infiniteLoopingThreads = new HashMap<>();
        this.commonWaitingThreads = new HashMap<>();
        this.commonBlockedThreads = new HashMap<>();
        this.commonTimedWaitingThreads = new HashMap<>();
    }

    public ArrayList<SingleThreadDump> getLisOfSingleThreadDump() {
        return lisOfSingleThreadDump;
    }

    public void setLisOfSingleThreadDump(ArrayList<SingleThreadDump> lisOfSingleThreadDump) {
        this.lisOfSingleThreadDump = lisOfSingleThreadDump;
    }

    public HashMap<Integer, ArrayList<String>> getInfiniteLoopingThreads() {
        return infiniteLoopingThreads;
    }

    public void setInfiniteLoopingThreads(HashMap<Integer, ArrayList<String>> infiniteLoopingThreads) {
        this.infiniteLoopingThreads = infiniteLoopingThreads;
    }

    public HashMap<Integer, ArrayList<String>> getCommonWaitingThreads() {
        return commonWaitingThreads;
    }

    public void setCommonWaitingThreads(HashMap<Integer, ArrayList<String>> commonWaitingThreads) {
        this.commonWaitingThreads = commonWaitingThreads;
    }

    public HashMap<Integer, ArrayList<String>> getCommonBlockedThreads() {
        return commonBlockedThreads;
    }

    public void setCommonBlockedThreads(HashMap<Integer, ArrayList<String>> commonBlockedThreads) {
        this.commonBlockedThreads = commonBlockedThreads;
    }

    public HashMap<Integer, ArrayList<String>> getCommonTimedWaitingThreads() {
        return commonTimedWaitingThreads;
    }

    public void setCommonTimedWaitingThreads(HashMap<Integer, ArrayList<String>> commonTimedWaitingThreads) {
        this.commonTimedWaitingThreads = commonTimedWaitingThreads;
    }
}
