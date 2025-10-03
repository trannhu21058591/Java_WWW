package iuh.fit.se.lab_5.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "DIENTHOAI")
public class DienThoai {
    @Id
    private String madt;

    private String tendt;
    private int namsanxuat;
    private String cauhinh;
    private String hinhanh;

    @ManyToOne
    @JoinColumn(name = "mancc")
    private NhaCungCap nhaCungCap;

    public DienThoai() {
    }

    public DienThoai(String madt, String tendt, int namsanxuat, String cauhinh, String hinhanh, NhaCungCap nhaCungCap) {
        this.madt = madt;
        this.tendt = tendt;
        this.namsanxuat = namsanxuat;
        this.cauhinh = cauhinh;
        this.hinhanh = hinhanh;
        this.nhaCungCap = nhaCungCap;
    }

    public String getMadt() {
        return madt;
    }

    public void setMadt(String madt) {
        this.madt = madt;
    }

    public String getTendt() {
        return tendt;
    }

    public void setTendt(String tendt) {
        this.tendt = tendt;
    }

    public int getNamsanxuat() {
        return namsanxuat;
    }

    public void setNamsanxuat(int namsanxuat) {
        this.namsanxuat = namsanxuat;
    }

    public String getCauhinh() {
        return cauhinh;
    }

    public void setCauhinh(String cauhinh) {
        this.cauhinh = cauhinh;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
}
