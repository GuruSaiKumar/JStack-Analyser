package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.model.SingleThreadDump;
import com.sprinklr.JStack.Analyser.repositaries.CombinedThreadDumpRepo;
import com.sprinklr.JStack.Analyser.repositaries.SingleThreadDumpRepo;
import com.sprinklr.JStack.Analyser.utils.CombinedThreadDumpUtil;
import com.sprinklr.JStack.Analyser.utils.SingleThreadDumpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ThreadDumpServiceImpl implements ThreadDumpService {
    private final CombinedThreadDumpRepo combinedThreadDumpRepo;
    private final SingleThreadDumpRepo singleThreadDumpRepo;

    //    Auto wiring constructor means auto wiring all properties
    @Autowired
    public ThreadDumpServiceImpl(CombinedThreadDumpRepo combinedThreadDumpRepo, SingleThreadDumpRepo singleThreadDumpRepo) {
        this.combinedThreadDumpRepo = combinedThreadDumpRepo;
        this.singleThreadDumpRepo = singleThreadDumpRepo;
    }

    @Override
    public CombinedThreadDump convertToWorkableFormat(String str, String regex, String saveToDb) {
        String[] lines = str.split("\\r?\\n");
        ArrayList<String[]> allDumpsData = getAllDumpsData(lines);

        CombinedThreadDump combinedThreadDump = new CombinedThreadDump();
        for (String[] eachDumpData : allDumpsData) {
            SingleThreadDump singleThreadDump = new SingleThreadDump();
            //Instead of processing everything in model we do computation in Util classes
            SingleThreadDumpUtil.buildSingleThreadDump(singleThreadDump,eachDumpData,regex);
            CombinedThreadDumpUtil.addSingleThreadDump(combinedThreadDump,singleThreadDump);
        }
        //After adding all the singleThreadDumps analyse common props.
            CombinedThreadDumpUtil.analyseCommonStuff(combinedThreadDump);
        //If saveToDB == "check" then this request is coming from cronjob
        if(Objects.equals(saveToDb, "check")){
            //Check if our dump crosses threshold.
            if(crossingThreshold(combinedThreadDump)){
                saveToDb = "true";
            }
        }
        if(Objects.equals(saveToDb, "true")) {
            for(SingleThreadDump singleThreadDump : combinedThreadDump.getLisOfSingleThreadDump()){
                singleThreadDumpRepo.save(singleThreadDump);
            }
            combinedThreadDumpRepo.save(combinedThreadDump);
        }
        return combinedThreadDump;
    }

    @Override
    public Optional<CombinedThreadDump> getCombinedThreadDump(String id) {
        return combinedThreadDumpRepo.findById(id);
    }

    @Override
    public CombinedThreadDump editOutputUsingParams(CombinedThreadDump originalResult, List<String> params) {
        if (params.contains("all")) return originalResult;
        CombinedThreadDump editedResult = new CombinedThreadDump();
        editOutputOfCombinedDumps(originalResult, editedResult, params);
        editOutputOfSingDumps(originalResult, editedResult, params);
        return editedResult;
    }

    private void editOutputOfCombinedDumps(CombinedThreadDump originalResult, CombinedThreadDump editedResult, List<String> params) {
        if (params.contains("infiniteLoopingThreads"))
            editedResult.setInfiniteLoopingThreads(originalResult.getInfiniteLoopingThreads());
        if (params.contains("commonWaitingThreads"))
            editedResult.setCommonWaitingThreads(originalResult.getCommonWaitingThreads());
        if (params.contains("commonBlockedThreads"))
            editedResult.setCommonBlockedThreads(originalResult.getCommonBlockedThreads());
        if (params.contains("commonTimedWaitingThreads"))
            editedResult.setCommonTimedWaitingThreads(originalResult.getCommonTimedWaitingThreads());
    }

    private void editOutputOfSingDumps(CombinedThreadDump originalResult, CombinedThreadDump editedResult, List<String> params) {
        //Initialise the List of single dump
        ArrayList<SingleThreadDump> editedListOfAllSingleDump = new ArrayList<>();
        for (SingleThreadDump curOriginalDump : originalResult.getLisOfSingleThreadDump()) {
            SingleThreadDump curEditedDump = new SingleThreadDump();
            if (params.contains("allThreads"))
                curEditedDump.setAllThreads(curOriginalDump.getAllThreads());
            if (params.contains("allStackTraces"))
                curEditedDump.setAllStackTraces(curOriginalDump.getAllStackTraces());
            if (params.contains("hashIdToThreadIds"))
                curEditedDump.setHashIdToThreadIds(curOriginalDump.getHashIdToThreadIds());
            if (params.contains("prefMatching"))
                curEditedDump.setPrefMatching(curOriginalDump.getPrefMatching());
            if (params.contains("allMethods"))
                curEditedDump.setAllMethods(curOriginalDump.getAllMethods());
            if (params.contains("methodHashIdToThreadIds"))
                curEditedDump.setMethodHashIdToThreadIds(curOriginalDump.getMethodHashIdToThreadIds());
            if (params.contains("deadLockedThreadNames"))
                curEditedDump.setDeadLockedThreadNames(curOriginalDump.getDeadLockedThreadNames());
            if (params.contains("deadLockedThreadIds"))
                curEditedDump.setDeadLockedThreadIds(curOriginalDump.getDeadLockedThreadIds());
            if (params.contains("mapStackTraceLength"))
                curEditedDump.setMapStackTraceLength(curOriginalDump.getMapStackTraceLength());
            if (params.contains("statistics"))
                curEditedDump.setStatistics(curOriginalDump.getStatistics());

            editedListOfAllSingleDump.add(curEditedDump);
        }
        editedResult.setLisOfSingleThreadDump(editedListOfAllSingleDump);
    }


    //HELPER FUNCTIONS

    private ArrayList<String[]> getAllDumpsData(String[] lines) {
        ArrayList<Integer> indices = new ArrayList<>();
        String startOfEachDump = "Full thread dump";
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].indexOf(startOfEachDump) == 0) {
                indices.add(i - 1);//i-1 since we have date at prev line.
            }
        }
        indices.add(lines.length);
        System.out.println(indices);
        ArrayList<String[]> allDumps = new ArrayList<>();
        for (int i = 0; i < indices.size() - 1; i++) {
            String[] singleDump = Arrays.copyOfRange(lines, indices.get(i), indices.get(i + 1));
            allDumps.add(singleDump);
        }
        return allDumps;
    }

    private boolean crossingThreshold(CombinedThreadDump combinedThreadDump){
        boolean crossingThreshold = false;
        int numberOfDumps = combinedThreadDump.getLisOfSingleThreadDump().size();
        //Check thread count
        long totalThreadCount = 0;
        boolean deadLockPresent = false;
        long commonBlockedThreads = combinedThreadDump.getCommonBlockedThreads().size();

        for(SingleThreadDump singleThreadDump : combinedThreadDump.getLisOfSingleThreadDump()){
            totalThreadCount+=singleThreadDump.getAllThreads().size();
            deadLockPresent = (deadLockPresent || (singleThreadDump.getDeadLockedThreadIds().size()>0));
        }
        //Average threadCount > 1000
        if(totalThreadCount/numberOfDumps > 0) crossingThreshold = true;
        if(deadLockPresent) crossingThreshold = true;
        if(commonBlockedThreads>10) crossingThreshold = true;
        return crossingThreshold;
    }
}
