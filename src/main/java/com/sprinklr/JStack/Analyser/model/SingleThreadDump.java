package com.sprinklr.JStack.Analyser.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SingleThreadDump {
    private String name;
    private HashMap<String,SingleThread> allThreads;// tid -> singleThread
    private HashMap<Integer, ArrayList<String>> allStackTraces;//hashId -> StackTrace
    private HashMap<Integer,ArrayList<String>> freqOfStackTraces;//hashId -> list of tid
    private Statistics statistics;

    SingleThreadDump() {
    }

    public SingleThreadDump(String[] eachDumpData) {
        this.name = eachDumpData[0] + '\n' + eachDumpData[1];
        this.allStackTraces = new HashMap<>();
        this.freqOfStackTraces = new HashMap<>();
        this.allThreads = new HashMap<>();
        buildAllThreads(eachDumpData);
        //TODO: Change Statistics
//        this.statistics = new Statistics(allThreads);
    }

    private void buildAllThreads(String[] eachDumpData) {
        ArrayList<SingleThread> result = new ArrayList<>();
        ArrayList<Integer> firstIndexOfThreads = new ArrayList<>();
        for (int i = 0; i < eachDumpData.length; i++) {
            //Start of new thread
            if (eachDumpData[i].indexOf('\"') == 0)
                firstIndexOfThreads.add(i);
        }
        for (int i = 0; i < firstIndexOfThreads.size(); i++) {
            int startIndex = firstIndexOfThreads.get(i);
            int lastIndex = startIndex + 2;//adding 2 here since we access second line in SingleThread Constructor
            if (i + 1 < firstIndexOfThreads.size())
                lastIndex = firstIndexOfThreads.get(i + 1);
            String[] currentThreadData = Arrays.copyOfRange(eachDumpData, startIndex, lastIndex);

            SingleThread currentThread = new SingleThread(currentThreadData);
            ArrayList<String> currentStackTrace = currentThread.getStackTrace();
            String threadId = currentThread.tid;
            int hashId = getHashIdOfStackTrace(currentStackTrace);

            currentThread.setHashId(hashId);
            currentThread.setStackTrace(null);//We don't need it anymore

            allThreads.put(threadId,currentThread);
            allStackTraces.putIfAbsent(hashId,currentStackTrace);
            freqOfStackTraces.putIfAbsent(hashId,new ArrayList<>());
            freqOfStackTraces.get(hashId).add(threadId);

        }
    }

    private int getHashIdOfStackTrace(ArrayList<String> stackTrace) {
        int hashId = stackTrace.hashCode();
        return hashId;
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

    public HashMap<Integer, ArrayList<String>> getFreqOfStackTraces() {
        return freqOfStackTraces;
    }

    public void setFreqOfStackTraces(HashMap<Integer, ArrayList<String>> freqOfStackTraces) {
        this.freqOfStackTraces = freqOfStackTraces;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
