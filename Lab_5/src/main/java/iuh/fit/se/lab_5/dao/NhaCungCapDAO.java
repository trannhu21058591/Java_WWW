package iuh.fit.se.lab_5.dao;

import iuh.fit.se.lab_5.entities.NhaCungCap;

import java.util.List;

public interface NhaCungCapDAO {
    List<NhaCungCap> findAll();
    NhaCungCap findById(String mancc);
    void save(NhaCungCap nhaCungCap);
    void delete(String mancc);
}
