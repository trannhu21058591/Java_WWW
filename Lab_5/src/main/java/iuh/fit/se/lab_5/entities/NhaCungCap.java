package iuh.fit.se.lab_5.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "NHACUNGCAP")
public class NhaCungCap {
    @Id
    private String mancc;

    private String tennhacc;
    private String diachi;
    private String sodienthoai;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL)
    private List<DienThoai> dienThoais;

    public NhaCungCap() {
    }

    public NhaCungCap(String mancc, String tennhacc, String diachi, String sodienthoai, List<DienThoai> dienThoais) {
        this.mancc = mancc;
        this.tennhacc = tennhacc;
        this.diachi = diachi;
        this.sodienthoai = sodienthoai;
        this.dienThoais = dienThoais;
    }

    public String getMancc() {
        return mancc;
    }

    public void setMancc(String mancc) {
        this.mancc = mancc;
    }

    public String getTennhacc() {
        return tennhacc;
    }

    public void setTennhacc(String tennhacc) {
        this.tennhacc = tennhacc;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public List<DienThoai> getDienThoais() {
        return dienThoais;
    }

    public void setDienThoais(List<DienThoai> dienThoais) {
        this.dienThoais = dienThoais;
    }
}
