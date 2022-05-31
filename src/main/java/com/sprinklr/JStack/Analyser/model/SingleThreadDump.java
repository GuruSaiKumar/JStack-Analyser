package com.sprinklr.JStack.Analyser.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class SingleThreadDump {
    private String name;
    private ArrayList<SingleThread> allThreads;

    SingleThreadDump() {
    }

    public SingleThreadDump(String[] eachDumpData) {
        this.name = eachDumpData[0] + '\n' + eachDumpData[1];
        this.allThreads = getAllThreads(eachDumpData);
    }

    private ArrayList<SingleThread> getAllThreads(String[] eachDumpData) {
        ArrayList<SingleThread> result = new ArrayList<>();
        ArrayList<Integer> firstIndexOfThreads = new ArrayList<>();
        for(int i = 0; i< eachDumpData.length; i++){
            //Start of new thread
            if(eachDumpData[i].indexOf('\"') == 0)
                firstIndexOfThreads.add(i);
        }
        //TODO: Handle the last thread
        for(int i = 0;i<firstIndexOfThreads.size()-1;i++){
            int startIndex = firstIndexOfThreads.get(i);
            int lastIndex = startIndex + 1;
            if(i+1 < firstIndexOfThreads.size())
                lastIndex = firstIndexOfThreads.get(i + 1);
            String[] currentThreadData = Arrays.copyOfRange(eachDumpData,startIndex,lastIndex);
            SingleThread currentThread = new SingleThread(currentThreadData);
            result.add(currentThread);
        }
        return result;
    }
}
