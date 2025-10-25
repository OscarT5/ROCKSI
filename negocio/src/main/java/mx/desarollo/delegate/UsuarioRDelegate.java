package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.UsuarioRDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Usuariorecepcionista;

import java.util.List;

public class UsuarioRDelegate {
    private final UsuarioRDAO UsuarioRDAO;

    public UsuarioRDelegate() {
        this.UsuarioRDAO = ServiceLocator.getInstanceURDAO();
    }

    public Usuariorecepcionista obtenerUR(String id) {
        try {
            if (id == null) return null;
            id = id.trim();
            if (id.isEmpty()) return null;

            Usuariorecepcionista ur = UsuarioRDAO.buscarURPorId(id);

            return ur;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo al usuario recepcionista con id=" + id, e);
        }
    }

    public List<Usuariorecepcionista> listarUR() {
        return UsuarioRDAO.findAllWithUsuarioR();
    }

}
