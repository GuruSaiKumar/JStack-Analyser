package com.sprinklr.JStack.Analyser.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {
    private final HashMap<String, ArrayList<Integer>> threadType;
    private  ArrayList<Integer> daemonThreads;
    private  ArrayList<Integer> nonDaemonThreads;

    Statistics(ArrayList<SingleThread> allThreads){
        this.threadType = new HashMap<String,ArrayList<Integer>>();
        this.daemonThreads = new ArrayList<>();
        this.nonDaemonThreads = new ArrayList<>();
        //TODO: Try other ways of calling processThreads
        processThreads(allThreads);
    }

    public void processThreads(ArrayList<SingleThread> allThreads){
            for(int index = 0; index <allThreads.size(); index++){
                SingleThread curThread = allThreads.get(index);
                //Getting state
                String state = curThread.getState();
                this.threadType.putIfAbsent(state, new ArrayList<Integer>());
                this.threadType.get(state).add(index);
                //Daemon Vs Non Daemon
                if(curThread.isDaemon()){
                    daemonThreads.add(index);
                }
                else{
                    nonDaemonThreads.add(index);
                }
            }
    }

    public HashMap<String, ArrayList<Integer>> getThreadType() {
        return threadType;
    }

    public ArrayList<Integer> getDaemonThreads() {
        return daemonThreads;
    }

    public void setDaemonThreads(ArrayList<Integer> daemonThreads) {
        this.daemonThreads = daemonThreads;
    }

    public ArrayList<Integer> getNonDaemonThreads() {
        return nonDaemonThreads;
    }

    public void setNonDaemonThreads(ArrayList<Integer> nonDaemonThreads) {
        this.nonDaemonThreads = nonDaemonThreads;
    }
}
