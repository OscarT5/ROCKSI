package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Usuariorecepcionista;

import java.util.List;

public class UsuarioRDAO extends AbstractDAO<Usuariorecepcionista> {
    private final EntityManager em;

    public UsuarioRDAO(EntityManager em) {
        super(Usuariorecepcionista.class);
        this.em = em;
    }

    public List<Usuariorecepcionista> findAllWithUsuarioR() {
        return execute(em -> {
            em.clear();

            List<Usuariorecepcionista> result = em.createQuery(
                            "SELECT Ur FROM Usuariorecepcionista Ur WHERE Ur.estatus = 1",
                            Usuariorecepcionista.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }

    public void actualizar(Usuariorecepcionista usuario) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            em.merge(usuario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el usuario recepcionista en la BD", e);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Usuariorecepcionista buscarURPorId(String id) {
        try {
            return em.find(Usuariorecepcionista.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el Usuario recepcionista por ID", e);
        }
    }

}
