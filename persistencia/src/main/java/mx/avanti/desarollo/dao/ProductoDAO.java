package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Producto;

public class ProductoDAO extends AbstractDAO<Producto> {
    private final EntityManager em;

    public ProductoDAO(EntityManager em) {
        super(Producto.class);
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Producto buscarProductoPorId(String id) {
        try {
            return em.find(Producto.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar producto por ID", e);
        }
    }

    public void actualizarProducto(Producto p) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) tx.begin();
            em.merge(p);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al modificar el producto.", e);
        }
    }
}
