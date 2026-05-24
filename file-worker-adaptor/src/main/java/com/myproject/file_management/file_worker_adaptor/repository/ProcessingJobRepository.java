package com.myproject.file_management.file_worker_adaptor.repository;

import com.myproject.file_management.file_worker_adaptor.model.ProcessingJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingJobRepository extends JpaRepository<ProcessingJob, String> {
}