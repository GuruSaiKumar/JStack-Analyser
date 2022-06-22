package com.sprinklr.JStack.Analyser.utils;

import com.sprinklr.JStack.Analyser.model.SingleThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SingleThreadUtil {

    public SingleThreadUtil() {
    }

    public static void buildSingleThread(SingleThread singleThread, String[] data) {
        int lastIndexOfThread = data.length;
        if (data[1].length() == 0) lastIndexOfThread = 2;//This case occurs at last thread.
        String[] stackTraceArray = Arrays.copyOfRange(data, 1, lastIndexOfThread);
        singleThread.setStackTrace(new ArrayList<>());
        singleThread.getStackTrace().addAll(Arrays.asList(stackTraceArray));

        String firstLine = data[0];
        singleThread.setName(getThreadName(singleThread, firstLine));

        singleThread.setDaemon(firstLine.contains("daemon"));
        singleThread.setTid(findByPrefix(singleThread, "tid=", firstLine));
        singleThread.setNid(findByPrefix(singleThread, "nid=", firstLine));
        singleThread.setPriority(convertToInt(singleThread, findByPrefix(singleThread, " prio=", firstLine)));
        singleThread.setOs_priority(convertToInt(singleThread, findByPrefix(singleThread, "os_prio=", firstLine)));

        String secondLine = data[1];//It is guaranteed to have at least 2 lines
        singleThread.setState(findByPrefix(singleThread, "java.lang.Thread.State: ", secondLine));
        singleThread.setHashId(-1);

        singleThread.setMethod(getMethodName(singleThread));
    }

    private static String getMethodName(SingleThread singleThread) {
        String result = "";
        //Method Exists if it has second line and is nonempty.
        if (singleThread.getStackTrace().size() >= 2 && singleThread.getStackTrace().get(1).length() > 0) {
            String secondLine = singleThread.getStackTrace().get(1);
            int index = secondLine.indexOf("at ");
            result = secondLine.substring(index + 3);//adding 3 to skip "at "
        }
        return result;
    }

    private static String getThreadName(SingleThread singleThread, String firstLine) {
        int first = firstLine.indexOf('\"');
        int last = firstLine.lastIndexOf('\"');
        return firstLine.substring(first + 1, last);
    }

    private static int convertToInt(SingleThread singleThread, String str) {
        if (Objects.equals(str, "")) return 0;
        return Integer.parseInt(str);
    }

    //finds prefix in str and then returns the part next to it
    private static String findByPrefix(SingleThread singleThread, String prefix, String str) {
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
