package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Usuarioadministrador;

import java.util.List;

public class UsuarioADao extends AbstractDAO<Usuarioadministrador> {

    private final EntityManager em;


    public UsuarioADao(EntityManager em) {

        super(Usuarioadministrador.class);
        this.em = em;
    }


    public List<Usuarioadministrador> findAllWithUsuarioA() {
        return execute(em -> {
            em.clear();

            List<Usuarioadministrador> result = em.createQuery(
                            "SELECT Ua FROM Usuarioadministrador Ua WHERE Ua.estatus = 1",
                            Usuarioadministrador.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }
    public List<Usuarioadministrador> listarActivos() {
        return execute(em -> {
            em.clear();
            return em.createQuery("SELECT u FROM Usuarioadministrador u WHERE u.estatus = 1", Usuarioadministrador.class).getResultList();
        });
    }

    public Usuarioadministrador buscarADMPorId(String id) {
        try {
            return em.find(Usuarioadministrador.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el Usuario Administrador por ID", e);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}