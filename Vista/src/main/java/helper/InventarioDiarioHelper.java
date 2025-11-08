package helper;

import mx.desarollo.integration.ServiceFacadeLocator;
import java.io.Serializable;

public class InventarioDiarioHelper implements Serializable {

    public void ejecutarSnapshotDiario() throws Exception {
        try {
            ServiceFacadeLocator.getInstanceInventarioDiarioFacade().ejecutarSnapshotDiario();
        } catch (RuntimeException e) { 
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error inesperado al ejecutar el snapshot: " + e.getMessage());
        }
    }
}