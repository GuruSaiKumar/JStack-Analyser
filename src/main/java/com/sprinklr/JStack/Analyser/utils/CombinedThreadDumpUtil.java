package com.sprinklr.JStack.Analyser.utils;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.model.SingleThreadDump;

import java.util.ArrayList;
import java.util.HashMap;

public class CombinedThreadDumpUtil {
    private final CombinedThreadDump combinedThreadDump;

    public CombinedThreadDumpUtil(CombinedThreadDump combinedThreadDump) {
        this.combinedThreadDump = combinedThreadDump;
    }

    public void analyseCommonStuff() {
        combinedThreadDump.setInfiniteLoopingThreads(getCommonThreadsWithState("RUNNABLE"));
        combinedThreadDump.setCommonWaitingThreads(getCommonThreadsWithState("WAITING"));
        combinedThreadDump.setCommonBlockedThreads(getCommonThreadsWithState("BLOCKED"));
        combinedThreadDump.setCommonTimedWaitingThreads(getCommonThreadsWithState("TIMED_WAITING"));
    }

    private HashMap<Integer, ArrayList<String>> getCommonThreadsWithState(String state) {
        //Final result StackTraceHashId -> (list of tid)
        HashMap<Integer, ArrayList<String>> commonThreads = new HashMap<>();
        ArrayList<SingleThreadDump> lisOfSingleThreadDump = combinedThreadDump.getLisOfSingleThreadDump();
        SingleThreadDump firstThreadDump = lisOfSingleThreadDump.get(0);
        ArrayList<String> threadsWithGivenStateInFirstDump = firstThreadDump.getStatistics().getThreadType().get(state);
        //Initially set common  thread ids to  thread ids in first dump having given state.
        ArrayList<String> commonThreadIds = new ArrayList<>();
        if (threadsWithGivenStateInFirstDump != null) {
            commonThreadIds.addAll(threadsWithGivenStateInFirstDump);
        }
        //Traverse other thread dumps and retain the common thread Ids having same state.
        for (int i = 1; i < lisOfSingleThreadDump.size(); i++) {
            SingleThreadDump currentThreadDump = lisOfSingleThreadDump.get(i);
            ArrayList<String> currentStateThreadIds = currentThreadDump.getStatistics().getThreadType().get(state);
            //NOTE: for using a.retainAll(b) both a and b must be not null.
            if (currentStateThreadIds == null) currentStateThreadIds = new ArrayList<>();
            commonThreadIds.retainAll(currentStateThreadIds);
        }
        for (String tid : commonThreadIds) {
            //If Daemon then ignore
            boolean isDaemon = firstThreadDump.getAllThreads().get(tid).isDaemon();
            if (isDaemon) continue;
            int hashId = firstThreadDump.getAllThreads().get(tid).getHashId();
            commonThreads.putIfAbsent(hashId, new ArrayList<>());
            commonThreads.get(hashId).add(tid);
        }
        return commonThreads;
    }

    public void addSingleThreadDump(SingleThreadDump singleThreadDump) {
        combinedThreadDump.getLisOfSingleThreadDump().add(singleThreadDump);
    }
}
