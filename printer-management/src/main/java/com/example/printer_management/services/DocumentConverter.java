package com.example.printer_management.services;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class DocumentConverter {

    public byte[] convertToPDF(byte[] fileContent, String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (fileName.endsWith(".docx")) {
            try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(fileContent))) {
                PDDocument pdfDoc = new PDDocument();
                PDPage page = new PDPage();
                pdfDoc.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText(doc.getDocument().getBody().getText());
                contentStream.endText();
                contentStream.close();
                pdfDoc.save(outputStream);
                pdfDoc.close();
            }
        } else if (fileName.endsWith(".xlsx")) {
            try (Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(fileContent))) {
                PDDocument pdfDoc = new PDDocument();
                PDPage page = new PDPage();
                pdfDoc.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText(wb.getSheetAt(0).getRow(0).getCell(0).toString());
                contentStream.endText();
                contentStream.close();
                pdfDoc.save(outputStream);
                pdfDoc.close();
            }
        }
        return outputStream.toByteArray();
    }
}
