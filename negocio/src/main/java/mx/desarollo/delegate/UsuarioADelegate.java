package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.UsuarioADao;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Usuarioadministrador;
import java.util.List;

public class UsuarioADelegate {
    private final UsuarioADao usuarioADao;

    public UsuarioADelegate() {
        // Asegúrate de tener getInstanceUADAO() en ServiceLocator
        this.usuarioADao = ServiceLocator.getInstanceUADAO();
    }




    public Usuarioadministrador obtenerUA(String id) {
        return usuarioADao.buscarADMPorId(id);
    }


    public Usuarioadministrador obtenerUsuarioAPorId(String id) {
        return usuarioADao.buscarADMPorId(id);
    }

    public List<Usuarioadministrador> listarUA() {
        return usuarioADao.listarActivos();
    }
}