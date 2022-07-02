package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;

import java.util.List;
import java.util.Optional;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str, String regex, String saveToDb);

    CombinedThreadDump editOutputUsingParams(CombinedThreadDump combinedThreadDump, List<String> params);

    Optional<CombinedThreadDump> getCombinedThreadDump(String id);
    void deleteCombinedThreadDump(String id);
}
