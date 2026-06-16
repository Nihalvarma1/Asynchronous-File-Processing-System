package com.myproject.email_trigger_service.service;

import java.io.File;

public interface EmailTriggerService {
    void processFile(File file);
    void sendEmailWithAttachment(File file);
}
