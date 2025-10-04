package iuh.fit.se.lab_5.dao;

import iuh.fit.se.lab_5.entities.DienThoai;

import java.util.List;

public interface DienThoaiDAO {
    List<DienThoai> findAll();
    void save(DienThoai dienThoai);
    void delete(String madt);
    List<DienThoai> findByNCC(String mancc);
}
