package com.sprinklr.JStack.Analyser.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class SingleThreadDump {
    private String name;
    private ArrayList<SingleThread> allThreads;

    private Statistics statistics;
    SingleThreadDump() {
    }

    public SingleThreadDump(String[] eachDumpData) {
        this.name = eachDumpData[0] + '\n' + eachDumpData[1];
        this.allThreads = getAllThreads(eachDumpData);
        this.statistics = new Statistics(allThreads);
    }

    private ArrayList<SingleThread> getAllThreads(String[] eachDumpData) {
        ArrayList<SingleThread> result = new ArrayList<>();
        ArrayList<Integer> firstIndexOfThreads = new ArrayList<>();
        for(int i = 0; i< eachDumpData.length; i++){
            //Start of new thread
            if(eachDumpData[i].indexOf('\"') == 0)
                firstIndexOfThreads.add(i);
        }
        for(int i = 0;i<firstIndexOfThreads.size();i++){
            int startIndex = firstIndexOfThreads.get(i);
            int lastIndex = startIndex + 2;//adding 2 here since we access second line in SingleThread Constructor
            if(i+1 < firstIndexOfThreads.size())
                lastIndex = firstIndexOfThreads.get(i + 1);
            String[] currentThreadData = Arrays.copyOfRange(eachDumpData,startIndex,lastIndex);
            SingleThread currentThread = new SingleThread(currentThreadData);
//            currentThread.setStackTrace(null);
            result.add(currentThread);
        }
        return result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SingleThread> getAllThreads() {
        return allThreads;
    }

    public void setAllThreads(ArrayList<SingleThread> allThreads) {
        this.allThreads = allThreads;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
