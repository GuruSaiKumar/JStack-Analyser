package com.sprinklr.JStack.Analyser.utils;

import com.sprinklr.JStack.Analyser.model.SingleThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SingleThreadUtil {
    private final SingleThread singleThread;

    public SingleThreadUtil(SingleThread singleThread) {
        this.singleThread = singleThread;
    }

    public void buildSingleThread(String[] data) {
        int lastIndexOfThread = data.length;
        if (data[1].length() == 0) lastIndexOfThread = 2;//This case occurs at last thread.
        String[] stackTraceArray = Arrays.copyOfRange(data, 1, lastIndexOfThread);
        singleThread.setStackTrace(new ArrayList<>());
        singleThread.getStackTrace().addAll(Arrays.asList(stackTraceArray));

        String firstLine = data[0];
        singleThread.setName(getThreadName(firstLine));

        singleThread.setDaemon(firstLine.contains("daemon"));
        singleThread.setTid(findByPrefix("tid=", firstLine));
        singleThread.setNid(findByPrefix("nid=", firstLine));
        singleThread.setPriority(convertToInt(findByPrefix(" prio=", firstLine)));
        singleThread.setOs_priority(convertToInt(findByPrefix("os_prio=", firstLine)));

        String secondLine = data[1];//It is guaranteed to have at least 2 lines
        singleThread.setState(findByPrefix("java.lang.Thread.State: ", secondLine));
        singleThread.setHashId(-1);

        singleThread.setMethod(getMethodName());
    }

    private String getMethodName() {
        String result = "";
        //Method Exists if it has second line and is nonempty.
        if (singleThread.getStackTrace().size() >= 2 && singleThread.getStackTrace().get(1).length() > 0) {
            String secondLine = singleThread.getStackTrace().get(1);
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
}
