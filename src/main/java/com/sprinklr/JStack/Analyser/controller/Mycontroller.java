package com.sprinklr.JStack.Analyser.controller;


import com.sprinklr.JStack.Analyser.model.CombinedThreadDump;
import com.sprinklr.JStack.Analyser.service.ThreadDumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class Mycontroller {
    @Autowired
    private ThreadDumpService threadDumpService;

    @GetMapping(value = "/api")
    String getHome() {
        return "Server is UP! ";
    }

    @PostMapping(value = "/api")
    public ResponseEntity<CombinedThreadDump> uploadFile(@RequestPart("file") MultipartFile file) {
        CombinedThreadDump combinedThreadDump = null;
        try {
            byte[] bytes = file.getBytes();
            String str = new String(bytes);
            combinedThreadDump = this.threadDumpService.convertToWorkableFormat(str);
            System.out.println((combinedThreadDump.toString()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(combinedThreadDump);
        return new ResponseEntity<CombinedThreadDump>(combinedThreadDump, HttpStatus.OK);
    }
    @GetMapping(value = "/api/all")
    List<CombinedThreadDump> getAll(){
        return threadDumpService.getAllCombinedThreadDumps();
    }
}
