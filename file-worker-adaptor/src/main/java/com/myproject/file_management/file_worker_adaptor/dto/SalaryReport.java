package com.myproject.file_management.file_worker_adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryReport {

    private int totalEmployees;

    private double averageSalary;

    private double highestSalary;

    private double lowestSalary;
}
