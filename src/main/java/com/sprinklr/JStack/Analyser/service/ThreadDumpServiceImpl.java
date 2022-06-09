package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.model.SingleThreadDump;
import com.sprinklr.JStack.Analyser.repositaries.CombinedThreadDumpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadDumpServiceImpl implements ThreadDumpService{
    private final CombinedThreadDumpRepo combinedThreadDumpRepo;

//    Auto wiring constructor means auto wiring all properties
    @Autowired
    public ThreadDumpServiceImpl( CombinedThreadDumpRepo combinedThreadDumpRepo) {
        this.combinedThreadDumpRepo = combinedThreadDumpRepo;
    }
    @Override
    public CombinedThreadDump convertToWorkableFormat(String str, String regex) {
        String[] lines = str.split("\\r?\\n");
        ArrayList<String[]> allDumpsData = getAllDumpsData(lines);

        CombinedThreadDump combinedThreadDump = new CombinedThreadDump();
        for(String[] eachDumpData : allDumpsData){
            SingleThreadDump singleThreadDump = new SingleThreadDump(eachDumpData,regex);
            combinedThreadDump.addSingleThreadDump(singleThreadDump);
        }
        //After adding all the singleThreadDumps analyse common props.
        combinedThreadDump.analyseCommonStuff();

//        combinedThreadDumpRepo.save(combinedThreadDump);
        return combinedThreadDump;
    }

    @Override
    public List<CombinedThreadDump> getAllCombinedThreadDumps() {
        return combinedThreadDumpRepo.findAll();
    }


    //HELPER FUNCTIONS

    private ArrayList<String[]> getAllDumpsData(String[] lines){
        ArrayList<Integer> indices = new ArrayList<>();
        String startOfEachDump = "Full thread dump";
        for(int i =0;i< lines.length;i++){
            if(lines[i].indexOf(startOfEachDump)==0){
                indices.add(i-1);//i-1 since we have date at prev line.
            }
        }
        indices.add(lines.length);
        System.out.println(indices);
        ArrayList<String[]> allDumps = new ArrayList<>();
        for(int i =0;i<indices.size()-1;i++){
            String[] singleDump = Arrays.copyOfRange(lines, indices.get(i), indices.get(i + 1));
            allDumps.add(singleDump);
        }
        return allDumps;
    }
}
