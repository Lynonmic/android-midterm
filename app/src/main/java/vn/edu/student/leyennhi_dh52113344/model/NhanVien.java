package vn.edu.student.leyennhi_dh52113344.model;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private int manv;
    private String tennv;
    private int maphongban;
    private String hinhanh;

    public NhanVien(int manv, String tennv, int maphongban, String hinhanh) {
        this.manv = manv;
        this.tennv = tennv;
        this.maphongban = maphongban;
        this.hinhanh = hinhanh;
    }

    public NhanVien() {
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public int getMaphongban() {
        return maphongban;
    }

    public void setMaphongban(int maphongban) {
        this.maphongban = maphongban;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
