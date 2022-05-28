package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.ThreadDumpData;
import com.sprinklr.JStack.Analyser.repositaries.ThreadDumpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadDumpServiceImpl implements ThreadDumpService{
    ArrayList<ThreadDumpData> list;
    private final ThreadDumpRepo threadDumpRepo;

//    Auto wiring constructor means auto wiring all properties
    @Autowired
    public ThreadDumpServiceImpl(ThreadDumpRepo threadDumpRepo) {
        this.threadDumpRepo = threadDumpRepo;

    }
    @Override
    public ThreadDumpData convertToWorkableFormat(String str) {
        System.out.println(str);
        long threadCount = str.chars().filter(num -> num == '"').count() /2;
        long runnable = countFreq("RUNNABLE",str) + countFreq("runnable  \n",str);
        long waiting = countFreq(" WAITING",str) + countFreq(" waiting on condition  \n",str);
        long timed_waiting = countFreq("TIMED_WAITING",str);

        ThreadDumpData threadDumpData = new ThreadDumpData();
        threadDumpData.setThreadCount(threadCount);
        threadDumpData.setRunnable(runnable);
        threadDumpData.setWaiting(waiting);
        threadDumpData.setTimed_waiting(timed_waiting);

        String parts[] = str.split("\"");
//        for(int i =1;i<20;i = i+2)
//            System.out.println(parts[i]);
//        threadDumpRepo.save(threadDumpData);
        return threadDumpData;
    }


    //HELPER FUNCTIONS

    //TODO: Use K.M.P Algo to optimise time complexity.
    //Time complexity - O(M*N)
    static int countFreq(String small, String big) {
        int M = small.length();
        int N = big.length();
        int res = 0;

        /* A loop to slide small[] one by one */
        for (int i = 0; i <= N - M; i++) {
            /* For current index i, check for pattern match */
            int j;
            for (j = 0; j < M; j++) {
                if (big.charAt(i + j) != small.charAt(j)) {
                    break;
                }
            }
            // if small[0...M-1] = big[i, i+1, ...i+M-1]
            if (j == M) {
                res++;
                j = 0;
            }
        }
        return res;
    }
}
