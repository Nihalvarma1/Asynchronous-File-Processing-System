package com.myproject.file_management.file_upload_adaptor.controller;

import com.myproject.file_management.file_upload_adaptor.dto.FileEventDto;
import com.myproject.file_management.file_upload_adaptor.model.ProcessingJob;
import com.myproject.file_management.file_upload_adaptor.repository.ProcessingJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Slf4j
public class FileUploadController {

    @Autowired
    private KafkaTemplate<String, FileEventDto> kafkaTemplate;

    @Autowired
    private ProcessingJobRepository repository;

    @Value("${file.upload-dir}")
    private String directoryPath;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        String jobId = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        log.info("Event occurred: File uploaded: {}",originalFilename);

        Thread.sleep(5000);

        //Converts File.csv to File_YYYYMMDDHHMMSS.csv
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = LocalDateTime.now().format(fileFormatter);
        String name = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = name + "_" + timestamp + extension;
        log.info("File renamed: {}",newFileName);

        //Verifying directory
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            log.info("Directory doesn't exist, Creating directory: {}", directoryPath);
            dir.mkdirs();
        }

        //Moving uploaded file to local path with renamed version
        String filePath = directoryPath + "/" + newFileName;
        file.transferTo(new File(filePath));
        log.info("File saved to directory path: {}",filePath);

        //Producer sending an Event to Kafka
        FileEventDto event = new FileEventDto(jobId, newFileName, directoryPath);
        kafkaTemplate.send("file-topic", event);
        log.info("Message Published to Kafka Topic. Job ID: {} ", jobId);

        //Saving File Upload Event to Database(DB)
        DateTimeFormatter createdTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String createdTime= LocalDateTime.now().format(createdTimeFormatter);
        ProcessingJob job = new ProcessingJob(
                jobId,
                newFileName,
                "UPLOADED",
                createdTime
        );
        repository.save(job);
        log.info("Saved Data to DB");

        return "File uploaded successfully. Job ID: " + jobId;
    }
}