package com.myproject.email_trigger_service.listener;


import com.myproject.email_trigger_service.service.EmailTriggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class CustomFileListener extends FileAlterationListenerAdaptor {

    @Autowired
    EmailTriggerService emailTriggerService;

    private boolean initialRun = true;

    public CustomFileListener(EmailTriggerService emailTriggerService) {
        this.emailTriggerService = emailTriggerService;
    }

    @Override
    public void onFileCreate(File file) {
        log.info("CustomFileListener triggered: {}", initialRun);
        try {
            log.info("Processing the file: {}", file.getName());
            emailTriggerService.processFile(file);
        } catch (Exception e) {
            log.info("Exception Occurred: {}", e.getMessage(), e);
        }
    }
}
