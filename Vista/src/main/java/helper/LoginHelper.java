package helper;

import mx.desarollo.entity.Usuarioadministrador;
import mx.desarollo.entity.Usuariorecepcionista;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class LoginHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    public Object autenticarUsuario(String id, String contrasena) throws Exception {
        if (id == null || id.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new Exception("El ID y la contraseña no pueden estar vacíos.");
        }

        String idUpper = id.trim().toUpperCase();

        if (idUpper.startsWith("ADM")) {
            Usuarioadministrador admin = ServiceFacadeLocator.getInstanceAAFacade().obtenerUsuarioAPorId(idUpper);

            if (admin != null) {
                if (admin.getEstatus() != 1) {
                    throw new Exception("El usuario administrador ha sido dado de baja del sistema.");
                }

                if (admin.getContrasena().equals(contrasena)) {
                    return admin;
                } else {
                    throw new Exception("Contraseña incorrecta para el Administrador.");
                }
            }
        }

        else if (idUpper.startsWith("UR")) {
            Usuariorecepcionista recepcionista = ServiceFacadeLocator.getInstanceURFacade().obtenerUsuarioRPorId(idUpper);

            if (recepcionista != null) {
                if (recepcionista.getEstatus() != 1) {
                    throw new Exception("El usuario recepcionista ha sido dado de baja del sistema.");
                }

                if (recepcionista.getContrasena().equals(contrasena)) {
                    return recepcionista;
                } else {
                    throw new Exception("Contraseña incorrecta para el Recepcionista.");
                }
            }
        }
        throw new Exception("ID de usuario no encontrado o formato inválido (debe iniciar con ADM o UR).");
    }
}