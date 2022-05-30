package com.sprinklr.JStack.Analyser.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class SingleThread {
    String name;
    String state;
    boolean isDaemon;
    int priority;
    int os_priority;
    String tid;
    String nid;
    String[] stackTrace;

    SingleThread() {

    }

    SingleThread(String[] data){
        int len = data.length;
        this.stackTrace = Arrays.copyOfRange(data,1,len);

        String firstLine = data[0];
        this.isDaemon = firstLine.contains("daemon");
        this.name = findByPrefix("\"",firstLine);
        this.tid = findByPrefix("tid=",firstLine);
        this.nid = findByPrefix("nid=",firstLine);
        this.priority = convertToInt(findByPrefix("prio=",firstLine));
        this.os_priority = convertToInt(findByPrefix("os_prio=",firstLine));
    }

    private int convertToInt(String str) {
        if(Objects.equals(str, "")) return 0;
        return Integer.parseInt(str);
    }

    private String findByPrefix(String prefix,String str) {
        StringBuilder result = new StringBuilder();
        int index = str.indexOf(prefix);
        if(index==-1) return result.toString();

        index+=prefix.length();
        result.append(str.charAt(index));
        while((index+1 < str.length()) && (str.charAt(index+1)!=' ')){
            index++;
            result.append(str.charAt(index));
        }
        return result.toString();
    }


}
