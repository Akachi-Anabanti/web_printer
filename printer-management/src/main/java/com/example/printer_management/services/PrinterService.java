package com.example.printer_management.services;

import com.example.printer_management.models.PrintJob;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
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
        attrSet.add(new Copies(1));

        Doc doc = new SimpleDoc(new ByteArrayInputStream(printJob.getFileContent()), flavor, null);
        DocPrintJob job = g2010Service.createPrintJob();
        
        job.print(doc, attrSet);
    }

    public boolean isCurrentlyPrinting() {
        return isPrinting;
    }

    public int getQueueSize() {
        return printQueue.size();
    }
}