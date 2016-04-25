package com.example.kelvinharron.qralarm;

/**
 * UNUSED CLASS FILE
 * <p/>
 * Created for use in the unused geo alarm feature that has not been implemented.
 * Created by Conor on 17/04/2016.
 */
public class ScanItem {

    private String code;
    private boolean scanned;

    public ScanItem() {
    }

    public ScanItem(String code, boolean scanned) {
        this.code = code;
        this.scanned = scanned;
    }

    public String getCode() {
        return code;
    }

    public void setName(String code) {
        this.code = code;
    }

    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }
}
