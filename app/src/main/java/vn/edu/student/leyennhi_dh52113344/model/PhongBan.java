package vn.edu.student.leyennhi_dh52113344.model;

import java.io.Serializable;

public class PhongBan implements Serializable {
    private int maphongban;
    private String tenPhongBan;

    public PhongBan(int maphongban, String tenPhongBan) {
        this.maphongban = maphongban;
        this.tenPhongBan = tenPhongBan;
    }

    public PhongBan() {
    }

    public int getMaphongban() {
        return maphongban;
    }

    public void setMaphongban(int maphongban) {
        this.maphongban = maphongban;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    @Override
    public String toString() {
        return  tenPhongBan;
    }
}
