package com.example.printer_management.models;

import java.time.LocalDateTime;

public class PrintJob {
    private String fileName;
    private byte[] fileContent;
    private LocalDateTime submissionTime;
    private String status;
    private PrintSettings printSettings;

    public PrintJob(String fileName, byte[] fileContent, PrintSettings printSettings) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.submissionTime = LocalDateTime.now();
        this.status = "QUEUED";
        this.printSettings = printSettings;
    }

    // Getters and setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public byte[] getFileContent() { return fileContent; }
    public void setFileContent(byte[] fileContent) { this.fileContent = fileContent; }
    public LocalDateTime getSubmissionTime() { return submissionTime; }
    public void setSubmissionTime(LocalDateTime submissionTime) { this.submissionTime = submissionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public PrintSettings getPrintSettings() {
        return printSettings;
    }
    public void setPrintSettings(PrintSettings printSettings) {
        this.printSettings = printSettings;
    }
}