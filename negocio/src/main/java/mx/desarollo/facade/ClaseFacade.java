package mx.desarollo.facade;

import mx.desarollo.delegate.ClaseDelegate;
import mx.desarollo.entity.Clase;

public class ClaseFacade {
    private final ClaseDelegate claseDelegate = new ClaseDelegate();

    public void registrarClase(Clase clase) throws Exception {
        try {
            claseDelegate.registrarClase(clase);
        } catch (Exception e) {
            throw new Exception("Error al registrar la clase: " + e.getMessage());
        }
    }

    public boolean eliminarClase(String idClase) throws Exception {
        try{
            return claseDelegate.eliminarClase(idClase);
        } catch(Exception e){
            throw new Exception("Error al eliminar la clase: " + e.getMessage());
        }
    }
}
