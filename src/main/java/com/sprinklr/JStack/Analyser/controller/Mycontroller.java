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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            assert fileName != null;
            Path path = Paths.get(fileName);
            //save the given file locally
            Files.write(path, bytes);
            //check if it's a zip file
            String extension = file.getContentType();
            //If it is a zip file then unzip it.
            assert extension != null;
            if (extension.equals("application/zip")) {
                path = unzipFileAt(path);
            }

            byte[] finalBytes = Files.readAllBytes(path);
            Files.delete(path);//delete the final txt file
            String str = new String(finalBytes);
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
    private Path unzipFileAt(Path path)throws IOException {
        // Creating a byte array as buffer for reading
        byte[] buffer = new byte[1024];
        Path finalPath = null;//path of unzipped file
        try {
            File unZipped = path.toFile();
            ZipInputStream zis = new ZipInputStream(new FileInputStream(unZipped));
            // Getting the zipped list entry
            ZipEntry ze = zis.getNextEntry();//Since we know that our zipped file consists of only single text file
            assert ze != null;
            String fileName = ze.getName();
            File newFile = new File(fileName);
            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            // read till there are characters
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            // Closing file output stream connections
            fos.close();
            // Closing all remaining connections
            zis.closeEntry();
            zis.close();
            finalPath = newFile.toPath();
            Files.delete(path);//Delete the .zip file
        } catch (IOException ex) {
            // using printStackTrace() method
            ex.printStackTrace();
        }
        return finalPath;//return the unzipped file path
    }
}
