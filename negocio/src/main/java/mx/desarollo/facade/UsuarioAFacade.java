package mx.desarollo.facade;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.UsuarioADao;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.delegate.UsuarioADelegate;
import mx.desarollo.entity.Usuarioadministrador;

import java.util.List;

public class UsuarioAFacade {
    private final UsuarioADelegate delegate = new UsuarioADelegate();

    private EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public List<Usuarioadministrador> listarUA() {
        return delegate.listarUA();
    }
    public Usuarioadministrador obtenerUsuarioAPorId(String id) { return delegate.obtenerUsuarioAPorId(id); }
    public Usuarioadministrador obtenerUA(String id) { return delegate.obtenerUA(id); }

}