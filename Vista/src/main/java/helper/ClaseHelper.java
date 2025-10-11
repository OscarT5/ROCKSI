package helper;

import mx.desarollo.entity.Clase;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class ClaseHelper implements Serializable {
    public void AltaClase(Clase cla) throws Exception {
        try {
            ServiceFacadeLocator.getInstanceClaseFacade().registrarClase(cla);
        } catch (Exception e) {
            throw new Exception("Error al registrar la clase: " + e.getMessage());
        }
    }

}
