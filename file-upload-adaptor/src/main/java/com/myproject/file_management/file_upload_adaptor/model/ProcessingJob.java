package com.myproject.file_management.file_upload_adaptor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String jobId;

    private String fileName;

    private String Status;

    private String createdTime;

}
