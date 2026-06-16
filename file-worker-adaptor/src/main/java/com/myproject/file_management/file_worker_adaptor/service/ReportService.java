package com.myproject.file_management.file_worker_adaptor.service;

import com.myproject.file_management.file_worker_adaptor.dto.FileEventDto;
import com.myproject.file_management.file_worker_adaptor.dto.SalaryReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ReportService {

    @Value("${file.report-dir}")
    private String reportDir;

    public void generateReport(String jobId, SalaryReport report, FileEventDto event) throws IOException {

        File reportPath = new File(reportDir);
        if (!reportPath.exists()) {
            log.info("Report directory doesn't exist, creating directory: {}",reportPath);
            reportPath.mkdir();
        }

        String filePath = reportPath +"/Employee_Summary_Report_"+ jobId + ".txt";
        String dateTime  = LocalDateTime.now().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Summary Report\t (Generated: "+dateTime+")\n");
            writer.write("====================\n");
            writer.write("Total Employees: " + report.getTotalEmployees() + "\n");
            writer.write("Average Salary: " + report.getAverageSalary() + "\n");
            writer.write("Highest Salary: " + report.getHighestSalary() + "\n");
            writer.write("Lowest Salary: " + report.getLowestSalary() + "\n");
        }

        log.info("Report generated for JobId: {}", jobId);
    }
}
