package com.myproject.file_management.file_worker_adaptor.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileArchiveService {

    public void moveCsvFileToArchive(String sourcePath) throws IOException {

        Path sourceDir = Paths.get(sourcePath);
        Path archiveDir = sourceDir.resolve(Paths.get("archive"));

        // Create archive directory if it doesn't exist
        if (!Files.exists(archiveDir)) {
            Files.createDirectories(archiveDir);
        }

        // Validate source directory
        if (!Files.exists(sourceDir) || !Files.isDirectory(sourceDir)) {
            throw new IllegalArgumentException("Invalid source directory: " + sourcePath);
        }

        try (Stream<Path> paths = Files.list(sourceDir)) {
            paths.filter(Files::isRegularFile)
                .filter(file -> file.toString().toLowerCase().endsWith(".csv"))
                .forEach(file -> {
                    try {
                        Path targetPath = archiveDir.resolve(file.getFileName());
                        Files.move(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Archived CSV: " + file.getFileName());
                    }
                    catch (IOException e) {
                        throw new RuntimeException("Failed to archive file: " + file.getFileName(),e);
                    }
                });
        }
    }
}