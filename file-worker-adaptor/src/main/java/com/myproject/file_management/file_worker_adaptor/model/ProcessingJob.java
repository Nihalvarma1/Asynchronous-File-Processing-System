package com.myproject.file_management.file_worker_adaptor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingJob {

    @Id
    private String jobId;

    private String fileName;

    private String Status;

    private String createdTime;

}
