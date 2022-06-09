package com.sprinklr.JStack.Analyser.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;

@Document(collection = "CombinedThreadDumpData")
public class CombinedThreadDump {
    private ArrayList<SingleThreadDump> lisOfSingleThreadDump;
    private HashMap<Integer,ArrayList<String >> infiniteLoopingThreads; // (stack trace)hashId -> list of tid
    private HashMap<Integer,ArrayList<String >> commonWaitingThreads;// (stack trace)hashId -> list of tid
    private HashMap<Integer,ArrayList<String >> commonBlockedThreads;// (stack trace)hashId -> list of tid
    private HashMap<Integer,ArrayList<String >> commonTimedWaitingThreads;// (stack trace)hashId -> list of tid

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

    public void addSingleThreadDump(SingleThreadDump singleThreadDump) {
        this.lisOfSingleThreadDump.add(singleThreadDump);
    }

    @Override
    public String toString() {
        return "CombinedThreadDump{" +
                "lisOfSingleThreadDump=" + lisOfSingleThreadDump +
                '}';
    }

    public void analyseCommonStuff() {
        this.infiniteLoopingThreads = getCommonThreadsWithState("RUNNABLE");
        this.commonWaitingThreads =  getCommonThreadsWithState("WAITING");
        this.commonBlockedThreads = getCommonThreadsWithState("BLOCKED");
        this.commonTimedWaitingThreads = getCommonThreadsWithState("TIMED_WAITING");
    }

    private HashMap<Integer,ArrayList<String>> getCommonThreadsWithState(String state){
        //Final result StackTraceHashId -> (list of tid)
        HashMap<Integer,ArrayList<String>> commonThreads = new HashMap<>();

        SingleThreadDump firstThreadDump = lisOfSingleThreadDump.get(0);
        ArrayList<String> threadsWithGivenStateInFirstDump = firstThreadDump.getStatistics().getThreadType().get(state);
        //Initially set common  thread ids to  thread ids in first dump having given state.
        ArrayList<String> commonThreadIds = (threadsWithGivenStateInFirstDump!=null)?
                new ArrayList<>(threadsWithGivenStateInFirstDump)//because constructor cannot be called with null.
                :new ArrayList<>();
        //Traverse other thread dumps and retain the common thread Ids having same state.
        for(int i = 1;i<lisOfSingleThreadDump.size();i++){
            SingleThreadDump currentThreadDump = lisOfSingleThreadDump.get(i);
            ArrayList<String> currentStateThreadIds =
                    currentThreadDump.getStatistics().getThreadType().get(state);
            //NOTE: for using a.retainAll(b) both a and b must be not null.
            if(currentStateThreadIds==null) currentStateThreadIds = new ArrayList<>();
            commonThreadIds.retainAll(currentStateThreadIds);
        }
        for( String tid : commonThreadIds){
            //If Daemon then ignore
            boolean isDaemon = firstThreadDump.getAllThreads().get(tid).isDaemon();
            if(isDaemon) continue;
            int hashId = firstThreadDump.getAllThreads().get(tid).getHashId();
            commonThreads.putIfAbsent(hashId,new ArrayList<>());
            commonThreads.get(hashId).add(tid);
//            this.infiniteLoopingThreads.get(hashId).add(firstThreadDump.getAllThreads().get(tid).getName());
//            ArrayList<String> stackTrace = firstThreadDump.getAllStackTraces().get(hashId);
//            this.infiniteLoopingThreads.putIfAbsent(hashId,stackTrace);
        }
        return commonThreads;
    }
}
