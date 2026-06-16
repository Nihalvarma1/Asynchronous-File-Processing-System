package com.myproject.email_trigger_service.service.impl;

import com.myproject.email_trigger_service.constants.EmailTriggerConstants;
import com.myproject.email_trigger_service.service.EmailTriggerService;
import com.myproject.email_trigger_service.util.FileNameValidator;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailTriggerServiceImpl implements EmailTriggerService {

    @Value("#{'${mail.to}'.split(',')}")
    List<String> mailTo;

    private final JavaMailSender mailSender;

    @Override
    public void processFile(File file) {
        if (!FileNameValidator.isValid(file.getName())) {
            log.warn("Invalid file name (or) file is not related to CAID: {}", file.getName());
            return;
        }

        try {
            log.info("Waiting until file is fully written...");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error occurred while waiting for file load: {}", e.getMessage(), e);
        }
        log.info("Filename validated");
        sendEmailWithAttachment(file);
    }

    @Async
    @Override
    public void sendEmailWithAttachment(File file) {
        int retries = 3;
        //Attempting to retry sending email if it fails
        while (retries-- > 0) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                //true-> Need Attachment
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setFrom(EmailTriggerConstants.MAIL_FROM);
                messageHelper.setTo(mailTo.toArray(new String[0]));
                messageHelper.setSubject(EmailTriggerConstants.MAIL_SUBJECT);
                messageHelper.setText(EmailTriggerConstants.MAIL_BODY,false);
                messageHelper.addAttachment(file.getName(), file);
                log.info("Attempting to send email with attachment: {}", file.getName());

                mailSender.send(message);

                log.info("Email sent successfully...");
                return;

            } catch (Exception e) {
                log.error("Email failed to send, Retries left: {}", retries, e);
            }
        }
        if(retries<=0)
            log.error("All retry attempts failed, Email not sent for file: {}", file.getName());
    }
}
