package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str);
}
