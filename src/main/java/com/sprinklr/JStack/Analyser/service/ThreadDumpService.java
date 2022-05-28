package com.sprinklr.JStack.Analyser.service;

import com.sprinklr.JStack.Analyser.model.ThreadDumpData;

public interface ThreadDumpService {
    ThreadDumpData convertToWorkableFormat(String str);
}
