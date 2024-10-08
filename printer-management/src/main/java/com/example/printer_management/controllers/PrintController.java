package com.example.printer_management.controllers;

import com.example.printer_management.models.PrintJob;
import com.example.printer_management.services.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    @Autowired
    private PrinterService printerService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            PrintJob printJob = new PrintJob(file.getOriginalFilename(), file.getBytes());
            printerService.queuePrintJob(printJob);

            String message = printerService.isCurrentlyPrinting() ?
                "File queued for printing. Current queue size: " + printerService.getQueueSize() :
                "File sent to printer.";

            return ResponseEntity.ok(message);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to process file: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getPrinterStatus() {
        String status = printerService.isCurrentlyPrinting() ?
            "Printer is currently busy. Queue size: " + printerService.getQueueSize() :
            "Printer is idle. Queue size: " + printerService.getQueueSize();

        return ResponseEntity.ok(status);
    }
}