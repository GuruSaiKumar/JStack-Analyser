package com.sprinklr.JStack.Analyser.controller;


import com.sprinklr.JStack.Analyser.model.ThreadDumpData;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class Mycontroller {
    @Autowired
    private ThreadDumpService threadDumpService;

    @GetMapping(value = "/api")
    String getHome(){
        return "Hello world";
    }
    @PostMapping(value = "/api")
    public ResponseEntity<ThreadDumpData> uploadFile(@RequestPart("file") MultipartFile file) {
        long threadCount = 0;
        ThreadDumpData threadDumpData = null;
        try {
            byte[] bytes = file.getBytes();
            String str = new String(bytes);
            Path path = Paths.get(file.getOriginalFilename());
//            Files.write(path, bytes);
            threadDumpData = threadDumpService.convertToWorkableFormat(str);
            System.out.println(path.getFileName());
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(threadDumpData, HttpStatus.OK);
    }
}
