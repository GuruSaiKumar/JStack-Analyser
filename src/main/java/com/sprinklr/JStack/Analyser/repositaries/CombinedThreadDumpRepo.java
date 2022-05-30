package com.sprinklr.JStack.Analyser.repositaries;

import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.model.ThreadDumpData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombinedThreadDumpRepo extends MongoRepository<CombinedThreadDump,Long> {
}
