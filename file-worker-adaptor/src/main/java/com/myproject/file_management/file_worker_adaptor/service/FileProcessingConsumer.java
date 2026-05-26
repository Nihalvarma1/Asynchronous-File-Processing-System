package com.myproject.file_management.file_worker_adaptor.service;

import com.myproject.file_management.file_worker_adaptor.dto.FileEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class FileProcessingConsumer {

    @Autowired
    private FileProcessingService processingService;

    @KafkaListener(
            topics = "file-topic",
            groupId = "file-upload-group"
    )
    public void consume(FileEventDto event)
            throws IOException {
        log.info("Message Received from Consumer: File-Upload-Adaptor\n{\n\t\"jobId\": \"{}\"\n\t\"fileName\": \"{}\"\n\t\"filePath\": \"{}\"\n}",event.getJobId(),event.getFileName(),event.getFilePath());

        processingService.processFile(event);
    }
}