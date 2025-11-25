package helper;

import mx.desarollo.entity.Usuarioadministrador;
import mx.desarollo.integration.ServiceFacadeLocator;
import java.io.Serializable;
import java.util.List;

public class UsuarioAHelper implements Serializable {

    /**
     * Metodo para hacer alta de un usuario administrador que llamara a la instancia de UsuarioAFacade
     * @Throws Si la base de datos rechaza el registro
     * @Param Objeto de tipo Usuarioadministrador
     */
    public void AltaUsuarioA(Usuarioadministrador ua) throws Exception {
        ServiceFacadeLocator.getInstanceAAFacade().registrarUsuarioAdministrador(ua);
    }

    public Usuarioadministrador obtenerUA(String id) {
        return ServiceFacadeLocator.getInstanceAAFacade().obtenerUA(id);
    }

    public void modificarUsuarioA(Usuarioadministrador ua) throws Exception {
        ServiceFacadeLocator.getInstanceAAFacade().modificarUsuarioA(ua);
    }

    public boolean bajaUsuarioA(String id) throws Exception {
        return ServiceFacadeLocator.getInstanceAAFacade().bajaUsuarioA(id);
    }

    public List<Usuarioadministrador> listarUA() {
        return ServiceFacadeLocator.getInstanceAAFacade().listarUA();
    }
}