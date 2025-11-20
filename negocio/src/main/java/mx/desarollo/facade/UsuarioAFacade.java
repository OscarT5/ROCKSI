package mx.desarollo.facade;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.UsuarioADao;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.Usuarioadministrador;

public class UsuarioAFacade {

    private EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public Usuarioadministrador obtenerUsuarioAPorId(String id) {
        EntityManager em = getEntityManager();
        try {
            UsuarioADao dao = new UsuarioADao(em);
            return dao.buscarADMPorId(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}