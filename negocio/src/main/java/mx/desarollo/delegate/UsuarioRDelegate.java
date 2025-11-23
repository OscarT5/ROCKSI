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

    public void modificarUsuarioR(Usuariorecepcionista id) throws Exception {
        // validaciones basicas
        if (id == null || id.getIdUsuariorecep() == null || id.getIdUsuariorecep().trim().isEmpty()) {
            throw new Exception("No se puede modificar un usuario sin identificación válida.");
        }
        if (id.getNombreCompleto() == null || id.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre completo es obligatorio.");
        }
        if (id.getCorreo() == null || !id.getCorreo().contains("@")) {
            throw new Exception("Ingrese un correo válido.");
        }
        if (id.getContrasena() == null || id.getContrasena().trim().isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía.");
        }

        UsuarioRDAO.actualizar(id);
    }

    public boolean bajaUsuarioR(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El ID del usuario es necesario para dar la baja.");
        }

        // se valida si existe el usuario
        Usuariorecepcionista ur = UsuarioRDAO.buscarURPorId(id);
        if (ur == null) {
            throw new Exception("El usuario con ID " + id + " no existe.");
        }

        if (ur.getEstatus() == 0) {
            throw new Exception("El usuario ya se encuentra dado de baja.");
        }

        return UsuarioRDAO.baja(id);
    }

    public List<Usuariorecepcionista> listarUR() {
        return UsuarioRDAO.findAllWithUsuarioR();
    }

}
