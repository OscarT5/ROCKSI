package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.UsuarioADao;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Usuarioadministrador;

import java.util.Date;
import java.util.List;

public class UsuarioADelegate {
    private final UsuarioADao usuarioADao;

    public UsuarioADelegate() {
        // Asegúrate de tener getInstanceUADAO() en ServiceLocator
        this.usuarioADao = ServiceLocator.getInstanceUADAO();
    }

    /**
     * Metodo para registrar un usuario administrador que llamara a la instancia de UsuarioADAO
     * @Throws Si la base de datos rechaza el registro
     * @params Un objeto de tipo Usuarioadministrador
     * @return void
     */
    public void registrarUsuarioAdministrador(Usuarioadministrador ua) throws Exception {
        //validaciones
        if (ua.getNombreCompleto() == null || ua.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }
        if (ua.getCorreo() == null || ua.getCorreo().trim().isEmpty()) {
            throw new Exception("El correo no puede estar vacio.");
        }

        if (ua.getContrasena() == null || ua.getContrasena().trim().isEmpty()) {
            throw new Exception("Se debe asignar una contraseña.");
        }

        if (!ua.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new Exception("El nombre solo puede contener letras y espacios.");
        }

        ua.setEstatus(1);
        usuarioADao.crearUsuarioA(ua);
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