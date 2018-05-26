package com.server_x.servlet;

import java.io.Serializable;

/**
 * Created by Dong Son on 13-Oct-17.
 */

public class User implements Serializable{
    private String ten;
    private String lop;
    private String mssv;
    private String malop;
    private String mahocphan;
    private String imei;

    public User(String ten, String lop, String mssv, String malop, String mahocphan, String imei) {
        this.ten = ten;
        this.lop = lop;
        this.mssv = mssv;
        this.malop = malop;
        this.mahocphan = mahocphan;
        this.imei = imei;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMahocphan() {
        return mahocphan;
    }

    public void setMahocphan(String mahocphan) {
        mahocphan = mahocphan;
    }
}
