package com.myproject.file_management.file_worker_adaptor.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEventDto {
    @Id
    private String jobId;
    private String fileName;
    private String filePath;
}



