package com.sprinklr.JStack.Analyser.model;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleThreadDump {
    private String name;
    private HashMap<String,SingleThread> allThreads;// tid -> singleThread
    private HashMap<Integer, ArrayList<String>> allStackTraces;//hashId -> StackTrace
    private HashMap<Integer,ArrayList<String>> hashIdToThreadIds;//hashId -> list of tid
    private HashMap<String,ArrayList<String>> prefMatching;// prefix -> list of tid
    private HashMap<Integer, String> allMethods;//methodHashId -> method(String)
    private HashMap<Integer,ArrayList<String>> methodHashIdToThreadIds;//methodHashId -> list of tid
    private Set<String> deadLockedThreadNames;
    private ArrayList<String> deadLockedThreadIds;
    private Statistics statistics;

    SingleThreadDump() {
    }

    public SingleThreadDump(String[] eachDumpData, String regex) {
        this.name = eachDumpData[0] + '\n' + eachDumpData[1];
        this.allStackTraces = new HashMap<>();
        this.hashIdToThreadIds = new HashMap<>();
        this.allThreads = new HashMap<>();
        this.prefMatching = new HashMap<>();
        this.allMethods = new HashMap<>();
        this.methodHashIdToThreadIds = new HashMap<>();
        this.deadLockedThreadNames = new HashSet<>();
        this.deadLockedThreadIds = new ArrayList<>();
        buildAllThreads(eachDumpData,regex);
        this.statistics = new Statistics(allThreads);
        processDeadLocks();
    }


    private void buildAllThreads(String[] eachDumpData,String regex) {
        Pattern pattern = Pattern.compile(regex);

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
            String threadId = currentThread.tid;

            //Checking is this thread belongs to deadlock in thread Dump.
            //Every thread has threadId except from the deadlock part
            if(threadId.equals("")){
                String curThreadName = currentThread.getName();
                deadLockedThreadNames.add(curThreadName);
                continue;//Do not process for further computation since it is already been done for this thread
            }
            //Checking the regex part
            String curThreadName = currentThread.getName();
            Matcher matcher = pattern.matcher(curThreadName);
            if(!matcher.find()) continue;

            ArrayList<String> currentStackTrace = currentThread.getStackTrace();
            int hashId = getHashIdOfStackTrace(currentStackTrace);

            currentThread.setHashId(hashId);
            currentThread.setStackTrace(null);//We don't need it anymore

            allThreads.put(threadId,currentThread);
            allStackTraces.putIfAbsent(hashId,currentStackTrace);
            hashIdToThreadIds.putIfAbsent(hashId,new ArrayList<>());
            hashIdToThreadIds.get(hashId).add(threadId);

            //prefix matching
            String prefix = getPrefix(currentThread.getName());
            if(prefix!=null){
                //Key of hashMap cannot contain '.'
                if(prefix.indexOf('.')!=-1){
                    System.out.println("THIS CANNOT BE USED AS A KEY: ");
                    System.out.println(currentThread.getName());
                    prefix = "miscellaneous";
                }
                prefMatching.putIfAbsent(prefix,new ArrayList<>());
                prefMatching.get(prefix).add(threadId);
            }

            //For getting most used method.
            String curMethod = currentThread.getMethod();
            if(curMethod.length()>0){
                int methodHash = getHashIdMethod(curMethod);
                allMethods.putIfAbsent(methodHash,curMethod);
                methodHashIdToThreadIds.putIfAbsent(methodHash,new ArrayList<>());
                methodHashIdToThreadIds.get(methodHash).add(threadId);
            }
        }
    }


    private void processDeadLocks() {
        //All the deadlocked threads will be BLOCKED state
        ArrayList<String> blocked = statistics.getThreadType().get("BLOCKED");
        if(blocked!=null){
            for( String curThreadId : blocked){
                String curThreadName = allThreads.get(curThreadId).getName();
                if(deadLockedThreadNames.contains(curThreadName)){
                    deadLockedThreadIds.add(curThreadId);
                }
            }
        }
    }
    private int getHashIdOfStackTrace(ArrayList<String> stackTrace) {
        int hashId = stackTrace.hashCode();
        return hashId;
    }
    private int getHashIdMethod(String method) {
        int methodHashId = method.hashCode();
        return methodHashId;
    }

    private String getPrefix(String threadName){
        int index = -1;
        index = threadName.indexOf('-');
        if(index!=-1){
            return threadName.substring(0,index);
        }
        index = threadName.indexOf('#');
        if(index!=-1){
            return threadName.substring(0,index);
        }
        return null;
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
}
