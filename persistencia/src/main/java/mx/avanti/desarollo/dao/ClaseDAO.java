package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Clase;

import java.util.List;
import java.util.Optional;

public class ClaseDAO extends AbstractDAO<Clase> {

    private final EntityManager entityManager;

    public ClaseDAO(EntityManager em) {
        super(Clase.class);
        this.entityManager = em;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }


    public Clase buscarClasePorId(String id) {
        try {
            return entityManager.find(Clase.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la clase por ID", e);
        }
    }

    public List<Clase> listarTodasLasClases() {
        return findAll();
    }

    public void actualizarClase(Clase cla) {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();

            // Iniciar transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualizar la clase existente
            entityManager.merge(cla);

            // Confirmar los cambios
            tx.commit();

        } catch (Exception e) {
            // Revertir la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar la clase.", e);
        }
    }
}
