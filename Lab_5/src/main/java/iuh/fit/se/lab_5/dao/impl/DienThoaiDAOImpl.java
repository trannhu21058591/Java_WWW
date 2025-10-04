package iuh.fit.se.lab_5.dao.impl;

import iuh.fit.se.lab_5.dao.DienThoaiDAO;
import iuh.fit.se.lab_5.entities.DienThoai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DienThoaiDAOImpl implements DienThoaiDAO {
    private final EntityManager entityManager;

    public DienThoaiDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<DienThoai> findAll() {
        TypedQuery<DienThoai> query =
                entityManager.createQuery("SELECT d FROM DienThoai d", DienThoai.class);
        return query.getResultList();
    }

    @Override
    public void save(DienThoai dienThoai) {
        entityManager.getTransaction().begin();
        entityManager.persist(dienThoai);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(String madt) {
        entityManager.getTransaction().begin();
        DienThoai dt = entityManager.find(DienThoai.class, madt);
        if (dt != null) {
            entityManager.remove(dt);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<DienThoai> findByNCC(String mancc) {
        TypedQuery<DienThoai> query = entityManager.createQuery(
                "SELECT d FROM DienThoai d WHERE d.nhaCungCap.mancc = :mancc", DienThoai.class);
        query.setParameter("mancc", mancc);
        return query.getResultList();
    }
}
