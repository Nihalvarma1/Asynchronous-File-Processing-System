package com.myproject.email_trigger_service.listener;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportFolderMonitor {
    @Autowired
    EmailTriggerService emailTriggerService;

    @Value("${file.upload.path}")
    private String directoryPath;

    private FileAlterationMonitor monitor = null;

    @PostConstruct
    public void listenForNewFile() throws Exception{
        try{
            log.info("Started File Monitoring");
            startFileMonitoring();
        }
        catch(Exception e)
        {
            log.info("Exception Occurred: {}",e.getMessage(),e);
        }
    }
    private void startFileMonitoring() throws Exception{
        try{
            // Start monitoring the directory
            Path directory = Paths.get(directoryPath);
            Files.createDirectories(directory);
            FileAlterationObserver observer = new FileAlterationObserver(directory.toFile());
            observer.addListener(new CustomFileListener(emailTriggerService));
            monitor = new FileAlterationMonitor();
            monitor.addObserver(observer);
            monitor.start();
            log.info("Monitoring the Directory Path: {}",directoryPath);
        }
        catch(Exception e)
        {
            log.info("Exception Occurred: {}",e.getMessage(),e);
        }
    }
}