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
                            "SELECT DISTINCT Ua FROM Usuarioadministrador Ua LEFT JOIN FETCH Ua.idUsuarioadmin",
                            Usuarioadministrador.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH") // forzar lectura desde la BD
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
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