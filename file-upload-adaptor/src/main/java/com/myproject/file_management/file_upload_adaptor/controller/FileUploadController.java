package com.myproject.file_management.file_upload_adaptor.controller;

import com.myproject.file_management.file_upload_adaptor.dto.FileEventDto;
import com.myproject.file_management.file_upload_adaptor.model.ProcessingJob;
import com.myproject.file_management.file_upload_adaptor.repository.ProcessingJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private KafkaTemplate<String, FileEventDto> kafkaTemplate;

    @Autowired
    private ProcessingJobRepository repository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        System.out.println(uploadDir);
        String jobId = UUID.randomUUID().toString();


//        File uploadDir = new File("uploads");

//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();

        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(fileFormatter);
        String name = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = name + "_" + timestamp + extension;
        String filePath = uploadDir + "/" + newFileName;
        file.transferTo(new File(filePath));

        DateTimeFormatter createdTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        ProcessingJob job = new ProcessingJob(
                jobId,
                newFileName,
                "UPLOADED",
                LocalDateTime.now().format(createdTimeFormatter)
        );

        repository.save(job);

        FileEventDto event = new FileEventDto(jobId, filePath);

        kafkaTemplate.send("file-topic", event);

        return "File uploaded successfully. Job ID: " + jobId;
    }
}