package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;

import java.util.List;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str);
    List<CombinedThreadDump> getAllCombinedThreadDumps();
}
