package com.example.printer_management.models;

public class PrintSettings {
    private String orientation;
    private String paperSize;
    private int scale;
    private String margins;
    private boolean backgroundGraphics;
    private String pageRange;


    public PrintSettings(String orientation, String paperSize, int scale, String margins,
                        boolean backgroundGraphics, String pageRange) {
        this.orientation = orientation;
        this.paperSize = paperSize;
        this.margins = margins;
        this.backgroundGraphics = backgroundGraphics;
        this.pageRange = pageRange;

    }

    public String getOrientation() { return orientation; }
    public String getPaperSize() { return paperSize; }
    public int getScale() { return scale; }
    public String getMargins() { return margins; }
    public boolean isBackgroundGraphics(){ return backgroundGraphics;}
    public String getPageRange() { return pageRange; }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }
    public void setScale(int scale) {
        this.scale = scale;
    }
    public void setMargins(String margins) {
        this.margins = margins;
    }
    public void setBackgroundGraphics(boolean backgroundGraphics) {
        this.backgroundGraphics = backgroundGraphics;
    }
    public void setPageRange(String pageRange) { this.pageRange = pageRange; }

}
