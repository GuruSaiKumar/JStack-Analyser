package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.model.ThreadDumpData;

public interface ThreadDumpService {
    CombinedThreadDump convertToWorkableFormat(String str);
}
