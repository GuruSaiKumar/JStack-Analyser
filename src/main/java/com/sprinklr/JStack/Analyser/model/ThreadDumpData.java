package com.sprinklr.JStack.Analyser.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "AllThreadDumpData")
public class ThreadDumpData {
    private long threadCount;
    //Can't use RUNNABLE since the tool which converts our model into JSON will make it lowercase.
    private long runnable;
    private long waiting;
    private long timed_waiting;

    public long getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(long threadCount) {
        this.threadCount = threadCount;
    }

    public long getRunnable() {
        return runnable;
    }

    public void setRunnable(long runnable) {
        this.runnable = runnable;
    }

    public long getWaiting() {
        return waiting;
    }

    public void setWaiting(long waiting) {
        this.waiting = waiting;
    }

    public long getTimed_waiting() {
        return timed_waiting;
    }

    public void setTimed_waiting(long timed_waiting) {
        this.timed_waiting = timed_waiting;
    }
}
