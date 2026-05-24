package com.myproject.file_management.file_worker_adaptor.service;


import com.myproject.file_management.file_worker_adaptor.dto.FileEventDto;
import com.myproject.file_management.file_worker_adaptor.model.ProcessingJob;
import com.myproject.file_management.file_worker_adaptor.repository.ProcessingJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileProcessingConsumer {

    @Autowired
    private ProcessingJobRepository repository;

    @KafkaListener(topics = "file-topic")
    public void consume(FileEventDto event)
            throws IOException {

        System.out.println("Received Event: " + event.getJobId());

        ProcessingJob job = repository
                .findById(event.getJobId())
                .orElseThrow();

        job.setStatus("PROCESSING");
        repository.save(job);

        List<String> lines = Files.readAllLines(
                Paths.get(event.getFilePath())
        );

        int totalEmployees = lines.size() - 1;

        double totalSalary = 0;
        double highestSalary = 0;
        double lowestSalary = Double.MAX_VALUE;

        for (int i = 1; i < lines.size(); i++) {

            String[] data = lines.get(i).split(",");

            double salary = Double.parseDouble(data[2]);

            totalSalary += salary;

            highestSalary = Math.max(highestSalary, salary);
            lowestSalary = Math.min(lowestSalary, salary);
        }

        double averageSalary = totalSalary / totalEmployees;

        File reportDir = new File("reports");

        if (!reportDir.exists()) {
            reportDir.mkdir();
        }

        String reportPath =
                "reports/report-" + event.getJobId() + ".txt";

        BufferedWriter writer =
                new BufferedWriter(new FileWriter(reportPath));

        writer.write("Processing Summary\n");
        writer.write("====================\n");
        writer.write("Total Employees: " + totalEmployees + "\n");
        writer.write("Average Salary: " + averageSalary + "\n");
        writer.write("Highest Salary: " + highestSalary + "\n");
        writer.write("Lowest Salary: " + lowestSalary + "\n");

        writer.close();

    }
}