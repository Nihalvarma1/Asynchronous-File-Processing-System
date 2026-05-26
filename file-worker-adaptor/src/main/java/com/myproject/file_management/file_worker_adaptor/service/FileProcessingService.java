package com.myproject.file_management.file_worker_adaptor.service;

import com.myproject.file_management.file_worker_adaptor.dto.FileEventDto;
import com.myproject.file_management.file_worker_adaptor.dto.SalaryReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class FileProcessingService {

    @Autowired
    private JobService jobService;

    @Autowired
    private ReportService reportService;

    public void processFile(FileEventDto event) throws IOException {

        jobService.updateStatus(event.getJobId(),"PROCESSING");
        String filePath= event.getFilePath()+"/"+event.getFileName();

        List<String> lines = Files.readAllLines(Paths.get(filePath));

        SalaryReport report = calculateSalary(lines);

        reportService.generateReport(event.getJobId(), report, event);
        jobService.updateStatus(event.getJobId(),"COMPLETED");
    }

    private SalaryReport calculateSalary(List<String> lines) {

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

        return new SalaryReport(totalEmployees, averageSalary, highestSalary, lowestSalary);
    }
}