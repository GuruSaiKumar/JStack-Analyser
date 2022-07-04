package com.sprinklr.JStack.Analyser.repositaries;

import com.sprinklr.JStack.Analyser.model.SingleThreadDump;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleThreadDumpRepo extends MongoRepository<SingleThreadDump,String> {
}
