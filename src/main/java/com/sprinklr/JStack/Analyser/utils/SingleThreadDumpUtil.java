package com.sprinklr.JStack.Analyser.utils;

import com.sprinklr.JStack.Analyser.model.SingleThread;
import com.sprinklr.JStack.Analyser.model.SingleThreadDump;
import com.sprinklr.JStack.Analyser.model.Statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleThreadDumpUtil {
    private final SingleThreadDump singleThreadDump;

    public SingleThreadDumpUtil(SingleThreadDump singleThreadDump) {
        this.singleThreadDump = singleThreadDump;
    }

    public void buildSingleThreadDump(String[] eachDumpData, String regex) {
        singleThreadDump.setName(eachDumpData[0] + '\n' + eachDumpData[1]);
        singleThreadDump.setMapStackTraceLength(initialiseMapStackTraceLen());
        buildAllThreads(eachDumpData, regex);
        Statistics statistics = new Statistics();
        StatisticsUtil statisticsUtil = new StatisticsUtil(statistics);
        statisticsUtil.buildStatistics(singleThreadDump.getAllThreads());
        this.singleThreadDump.setStatistics(statistics);
        processDeadLocks();
    }

    private HashMap<String, ArrayList<String>> initialiseMapStackTraceLen() {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        result.put("Below10", new ArrayList<>());
        result.put("Below100", new ArrayList<>());
        result.put("Above100", new ArrayList<>());
        return result;
    }


    private void buildAllThreads(String[] eachDumpData, String regex) {
        Pattern pattern = Pattern.compile(regex);

        ArrayList<Integer> firstIndexOfThreads = getStartingIndexOfAllThreads(eachDumpData);
        for (int i = 0; i < firstIndexOfThreads.size(); i++) {
            SingleThread currentThread = getSingleThreadAtIndex(eachDumpData, firstIndexOfThreads, i);
            String threadId = currentThread.getTid();

            //Checking if this thread belongs to deadlock in thread Dump.
            if (checkIfBelongsToDeadLock(currentThread, threadId))
                continue;//Do not process for further computation since it is already been done for this thread
            //Checking the regex part
            if (checkRegexPart(pattern, currentThread)) continue;

            ArrayList<String> currentStackTrace = currentThread.getStackTrace();
            int hashId = getHashIdOfStackTrace(currentStackTrace);

            currentThread.setHashId(hashId);
            currentThread.setStackTrace(null);//We don't need it anymore

            singleThreadDump.getAllThreads().put(threadId, currentThread);
            analyseCurrentStackTrace(threadId, currentStackTrace, hashId);
            //prefix matching
            doPrefixMatching(currentThread, threadId);
            //For getting most used method.
            checkCurrentMethod(currentThread, threadId);
            //For mapping stackTraces according to their length
            checkLengthOfStackTrace(threadId, currentStackTrace);

        }
    }

    private ArrayList<Integer> getStartingIndexOfAllThreads(String[] eachDumpData) {
        ArrayList<Integer> firstIndexOfThreads = new ArrayList<>();
        for (int i = 0; i < eachDumpData.length; i++) {
            //Start of new thread
            if (eachDumpData[i].indexOf('\"') == 0) firstIndexOfThreads.add(i);
        }
        return firstIndexOfThreads;
    }

    private SingleThread getSingleThreadAtIndex(String[] eachDumpData, ArrayList<Integer> firstIndexOfThreads, int i) {
        int startIndex = firstIndexOfThreads.get(i);
        int lastIndex = startIndex + 2;//adding 2 here since we access second line in SingleThread Constructor
        if (i + 1 < firstIndexOfThreads.size()) lastIndex = firstIndexOfThreads.get(i + 1);
        String[] currentThreadData = Arrays.copyOfRange(eachDumpData, startIndex, lastIndex);

        SingleThread singleThread = new SingleThread();
        SingleThreadUtil singleThreadUtil = new SingleThreadUtil(singleThread);
        singleThreadUtil.buildSingleThread(currentThreadData);
        return singleThread;
    }

    private void analyseCurrentStackTrace(String threadId, ArrayList<String> currentStackTrace, int hashId) {
        singleThreadDump.getAllStackTraces().putIfAbsent(hashId, currentStackTrace);
        singleThreadDump.getHashIdToThreadIds().putIfAbsent(hashId, new ArrayList<>());
        singleThreadDump.getHashIdToThreadIds().get(hashId).add(threadId);
    }

    private boolean checkIfBelongsToDeadLock(SingleThread currentThread, String threadId) {
        //Every thread has threadId except from the deadlock part
        if (threadId.equals("")) {
            String curThreadName = currentThread.getName();
            singleThreadDump.getDeadLockedThreadNames().add(curThreadName);
            return true;
        }
        return false;
    }

    private boolean checkRegexPart(Pattern pattern, SingleThread currentThread) {
        String curThreadName = currentThread.getName();
        Matcher matcher = pattern.matcher(curThreadName);
        return !matcher.find();
    }

    private void checkLengthOfStackTrace(String threadId, ArrayList<String> currentStackTrace) {
        int currentStackTraceLen = currentStackTrace.size();
        String group;
        if (currentStackTraceLen <= 10) {
            group = "Below10";
        } else if (currentStackTraceLen <= 100) {
            group = "Below100";
        } else {
            group = "Above100";
        }
        singleThreadDump.getMapStackTraceLength().get(group).add(threadId);
    }

    private void checkCurrentMethod(SingleThread currentThread, String threadId) {
        String curMethod = currentThread.getMethod();
        if (curMethod.length() > 0) {
            int methodHash = getHashIdMethod(curMethod);
            singleThreadDump.getAllMethods().putIfAbsent(methodHash, curMethod);
            singleThreadDump.getMethodHashIdToThreadIds().putIfAbsent(methodHash, new ArrayList<>());
            singleThreadDump.getMethodHashIdToThreadIds().get(methodHash).add(threadId);
        }
    }

    private void doPrefixMatching(SingleThread currentThread, String threadId) {
        String prefix = getPrefix(currentThread.getName());
        if (prefix != null) {
            //Key of hashMap cannot contain '.'
            if (prefix.indexOf('.') != -1) {
                System.out.println("THIS CANNOT BE USED AS A KEY: ");
                System.out.println(currentThread.getName());
                prefix = "miscellaneous";
            }
            singleThreadDump.getPrefMatching().putIfAbsent(prefix, new ArrayList<>());
            singleThreadDump.getPrefMatching().get(prefix).add(threadId);
        }
    }


    private void processDeadLocks() {
        //All the deadlocked threads will be BLOCKED state
        ArrayList<String> blocked = singleThreadDump.getStatistics().getThreadType().get("BLOCKED");
        if (blocked != null) {
            for (String curThreadId : blocked) {
                String curThreadName = singleThreadDump.getAllThreads().get(curThreadId).getName();
                if (singleThreadDump.getDeadLockedThreadNames().contains(curThreadName)) {
                    singleThreadDump.getDeadLockedThreadIds().add(curThreadId);
                }
            }
        }
    }

    private int getHashIdOfStackTrace(ArrayList<String> stackTrace) {
        return stackTrace.hashCode();
    }

    private int getHashIdMethod(String method) {
        return method.hashCode();
    }

    private String getPrefix(String threadName) {
        int index = threadName.indexOf('-');
        if (index != -1) {
            return threadName.substring(0, index);
        }
        index = threadName.indexOf('#');
        if (index != -1) {
            return threadName.substring(0, index);
        }
        return null;
    }
}
