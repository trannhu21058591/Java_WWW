package iuh.fit.se.lab_5.dao.impl;

import iuh.fit.se.lab_5.dao.NhaCungCapDAO;
import iuh.fit.se.lab_5.entities.NhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class NhaCungCapDAOImpl implements NhaCungCapDAO {
    private final EntityManager entityManager;

    public NhaCungCapDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<NhaCungCap> findAll() {
        TypedQuery<NhaCungCap> query = 
                entityManager.createQuery("SELECT n FROM NhaCungCap n", NhaCungCap.class);
        return query.getResultList();
    }

    @Override
    public NhaCungCap findById(String mancc) {
        return entityManager.find(NhaCungCap.class, mancc);
    }

    @Override
    public void save(NhaCungCap nhaCungCap) {
        entityManager.getTransaction().begin();
        entityManager.persist(nhaCungCap);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(String mancc) {
        entityManager.getTransaction().begin();
        NhaCungCap ncc = entityManager.find(NhaCungCap.class, mancc);
        if (ncc != null) {
            entityManager.remove(ncc);
        }
        entityManager.getTransaction().commit();
    }
}
