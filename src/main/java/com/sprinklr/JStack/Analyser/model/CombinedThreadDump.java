package com.sprinklr.JStack.Analyser.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Document(collection = "CombinedThreadDumpData")
public class CombinedThreadDump {
    private ArrayList<SingleThreadDump> lisOfSingleThreadDump;

    public CombinedThreadDump() {
        this.lisOfSingleThreadDump = new ArrayList<>();
    }

    public ArrayList<SingleThreadDump> getLisOfSingleThreadDump() {
        return lisOfSingleThreadDump;
    }

    public void setLisOfSingleThreadDump(ArrayList<SingleThreadDump> lisOfSingleThreadDump) {
        this.lisOfSingleThreadDump = lisOfSingleThreadDump;
    }

    public void addSingleThreadDump(SingleThreadDump singleThreadDump) {
        this.lisOfSingleThreadDump.add(singleThreadDump);
    }
}
