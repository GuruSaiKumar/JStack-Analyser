package com.sprinklr.JStack.Analyser.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

    public int getHashId() {
        return hashId;
    }

    public void setHashId(int hashId) {
        this.hashId = hashId;
    }

    //Default constructor is required for bean to initialise
    SingleThread() {

    }

    SingleThread(String[] data) {
        int len = data.length;
        String[] stackTraceArray = Arrays.copyOfRange(data, 1, len);
        this.stackTrace = new ArrayList<String>();
        this.stackTrace.addAll(Arrays.asList(stackTraceArray));

        String firstLine = data[0];
        this.name = getThreadName(firstLine);

        this.isDaemon = firstLine.contains("daemon");
        this.tid = findByPrefix("tid=", firstLine);
        this.nid = findByPrefix("nid=", firstLine);
        this.priority = convertToInt(findByPrefix(" prio=", firstLine));
        this.os_priority = convertToInt(findByPrefix("os_prio=", firstLine));

        String secondLine = data[1];//It is guaranteed to have at least 2 lines
        this.state = findByPrefix("java.lang.Thread.State: ", secondLine);
        this.hashId = -1;

        this.method = getMethodName();
    }

    private String getMethodName() {
        String result = "";
        //Method Exists if it has second line and is nonempty.
        if (stackTrace.size() >= 2 && stackTrace.get(1).length() > 0) {
            String secondLine = stackTrace.get(1);
            int index = secondLine.indexOf("at ");
            result = secondLine.substring(index + 3);//adding 3 to skip "at "
        }
        return result;
    }

    private String getThreadName(String firstLine) {
        int first = firstLine.indexOf('\"');
        int last = firstLine.lastIndexOf('\"');
        return firstLine.substring(first + 1, last);
    }

    private int convertToInt(String str) {
        if (Objects.equals(str, "")) return 0;
        return Integer.parseInt(str);
    }

    //finds prefix in str and then returns the part next to it
    private String findByPrefix(String prefix, String str) {
        StringBuilder result = new StringBuilder();
        int index = str.indexOf(prefix);
        if (index == -1) return result.toString();

        index += prefix.length();
        result.append(str.charAt(index));
        while ((index + 1 < str.length()) && (str.charAt(index + 1) != ' ')) {
            index++;
            result.append(str.charAt(index));
        }
        return result.toString();
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
}
