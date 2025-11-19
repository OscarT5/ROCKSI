package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.InventarioDiario;

import java.time.LocalDate;
import java.util.List;

public class InventarioDiarioDAO extends AbstractDAO<InventarioDiario> {

    private final EntityManager entityManager;

    public InventarioDiarioDAO(EntityManager em) {
        super(InventarioDiario.class);
        this.entityManager = em;
    }


    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public boolean existeSnapshotParaFecha(LocalDate fecha) {
        List<InventarioDiario> snapshotsEncontrados = super.findByOneParameter(fecha, "fecha");

        return snapshotsEncontrados != null && !snapshotsEncontrados.isEmpty();
    }

    public List<InventarioDiario> findByFecha(LocalDate fecha) {
        return super.findByOneParameter(fecha, "fecha");
    }
}