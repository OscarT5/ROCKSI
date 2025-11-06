package mx.desarollo.facade;

import mx.desarollo.delegate.InventarioDiarioDelegate;
import mx.desarollo.delegate.InventarioDiarioDelegate.SnapshotException;


public class InventarioDiarioFacade {

    private final InventarioDiarioDelegate inventarioDelegate;

    public InventarioDiarioFacade() {
        this.inventarioDelegate = new InventarioDiarioDelegate();
    }

    public void ejecutarSnapshotDiario() throws RuntimeException {
        try {
            inventarioDelegate.crearSnapshotDiario();
        } catch (SnapshotException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al generar el snapshot: " + e.getMessage(), e);
        }
    }
}