package iuh.fit.se.bai_1.dao;

import iuh.fit.se.bai_1.entities.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> findAll();
    void save(Product product);
    void delete(int id);
}
