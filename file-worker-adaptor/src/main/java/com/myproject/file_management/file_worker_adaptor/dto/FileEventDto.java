package com.myproject.file_management.file_worker_adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEventDto {
    private String jobId;
    private String filePath;
}

