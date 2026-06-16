package com.myproject.file_management.file_worker_adaptor.service;

import com.myproject.file_management.file_worker_adaptor.model.ProcessingJob;
import com.myproject.file_management.file_worker_adaptor.repository.ProcessingJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService {

    @Autowired
    private ProcessingJobRepository repository;

    public void updateStatus(
            String jobId,
            String status) {

        ProcessingJob job = repository.findById(jobId).orElseThrow();

        job.setStatus(status);

        repository.save(job);

        log.info("Job {} updated to {}", jobId, status);
    }
}