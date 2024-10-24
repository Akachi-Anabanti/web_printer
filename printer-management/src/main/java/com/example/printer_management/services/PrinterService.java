
package com.example.printer_management.services;

import com.example.printer_management.models.PrintJob;
import com.example.printer_management.models.PrintSettings;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;

// import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PrinterService {
    private final ConcurrentLinkedQueue<PrintJob> printQueue = new ConcurrentLinkedQueue<>();
    private boolean isPrinting = false;

    public void queuePrintJob(PrintJob printJob) {
        printQueue.offer(printJob);
    }

    @Scheduled(fixedRate = 5000) // Check queue every 5 seconds
    public void processQueue() {
        if (isPrinting || printQueue.isEmpty()) {
            return;
        }

        PrintJob job = printQueue.poll();
        if (job != null) {
            try {
                isPrinting = true;
                print(job);
                job.setStatus("COMPLETED");
            } catch (Exception e) {
                job.setStatus("FAILED");
                e.printStackTrace();
            } finally {
                isPrinting = false;
            }
        }
    }

    private void print(PrintJob printJob) throws Exception {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService g2010Service = null;

        for (PrintService service : services) {
            if (service.getName().toLowerCase().contains("g2010")) {
                g2010Service = service;
                break;
            }
        }

        if (g2010Service == null) {
            throw new Exception("G2010 series printer not found");
        }

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet attrSet = new HashPrintRequestAttributeSet();

        applyPrintSettings(attrSet, printJob.getPrintSettings());

        Doc doc = new SimpleDoc(new ByteArrayInputStream(printJob.getFileContent()), flavor, null);
        DocPrintJob job = g2010Service.createPrintJob();
        
        job.print(doc, attrSet);
    }

    private void applyPrintSettings(PrintRequestAttributeSet attrSet, PrintSettings settings) {
        if ("landscape".equalsIgnoreCase(settings.getOrientation())) {
            attrSet.add(OrientationRequested.LANDSCAPE);
        } else {
            attrSet.add(OrientationRequested.PORTRAIT);
        }

        switch (settings.getPaperSize().toLowerCase()) {
            case "a4":
                attrSet.add(MediaSizeName.ISO_A4);
                break;
            case "letter":
                attrSet.add(MediaSizeName.NA_LETTER);
                break;
            case "legal":
                attrSet.add(MediaSizeName.NA_LEGAL);
                break;
        }
        // attrSet.add(new PrintQuality(settings.getScale()));

        switch (settings.getMargins().toLowerCase()) {
            case "normal":
                attrSet.add(new MediaPrintableArea(0.25f, 0.25f, 8f, 10.5f, MediaPrintableArea.INCH));
                break;
            case "narrow":
                attrSet.add(new MediaPrintableArea(0.1f, 0.1f, 08.3f, 10.8f, MediaPrintableArea.INCH));
                break;
            case "wide":
                attrSet.add(new MediaPrintableArea(0.5f, 0.5f, 7.8f, 10f, MediaPrintableArea.INCH));
                break;
        }

        if (settings.getPageRange() != null && !settings.getPageRange().isEmpty()) {
            attrSet.add(new PageRanges(settings.getPageRange()));
        }
    }

    public boolean isCurrentlyPrinting() {
        return isPrinting;
    }

    public int getQueueSize() {
        return printQueue.size();
    }
}
