package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;

import java.util.List;
import java.util.Optional;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str, String regex);
    List<CombinedThreadDump> getAllCombinedThreadDumps();
}
