package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;

import java.util.List;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str, String regex);

    CombinedThreadDump editOutputUsingParams(CombinedThreadDump combinedThreadDump, List<String> params);
}
