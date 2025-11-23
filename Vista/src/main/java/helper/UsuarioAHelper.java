package helper;

import mx.desarollo.entity.Usuarioadministrador;
import mx.desarollo.integration.ServiceFacadeLocator;
import java.io.Serializable;
import java.util.List;

public class UsuarioAHelper implements Serializable {

    public Usuarioadministrador obtenerUA(String id) {
        return ServiceFacadeLocator.getInstanceAAFacade().obtenerUA(id);
    }

    public List<Usuarioadministrador> listarUA() {
        return ServiceFacadeLocator.getInstanceAAFacade().listarUA();
    }
}