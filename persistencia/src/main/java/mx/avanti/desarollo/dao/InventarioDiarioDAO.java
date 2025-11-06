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

    /**
     Verifica si ya existen snapshots guardados para una fecha en espeicfico.
     Utiliza el método heredado 'findByOneParameter' de AbstractDAO.
     retorna true si ya existen registros para ese día, false en caso de que no.
     */
    public boolean existeSnapshotParaFecha(LocalDate fecha) {
        List<InventarioDiario> snapshotsEncontrados = super.findByOneParameter(fecha, "fecha");

        return snapshotsEncontrados != null && !snapshotsEncontrados.isEmpty();
    }
}