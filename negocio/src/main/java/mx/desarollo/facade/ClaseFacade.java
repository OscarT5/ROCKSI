package mx.desarollo.facade;

import mx.desarollo.delegate.ClaseDelegate;
import mx.desarollo.entity.Clase;

import java.util.List;

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

    /**
     * Metodo para actualizar los datos de una clase que llamara a la instancia de ClaseDelegate
     * @Throws Si la base de datos rechaza la peticion, ya sea por valores invalidos
     * @Params Un Objeto de tipo Clase
     * @return void
     */
    public void actualizarClase(Clase cla) throws Exception {
        claseDelegate.actualizarClase(cla);
    }

    /**
     * Metodo para obtener una clase por su ID que llamara a la instancia de ClaseDelegate
     * @Throws Si la base de datos rechaza la peticion o no se ecuntra la clase con el ID
     * @Params Un String id de la clase
     * @return Un objeto de tipo Clase
     */
    public Clase obtenerClasePorId(String id) {
        return claseDelegate.obtenerClase(id);
    }

    public List<Clase> listarClases() {
        return claseDelegate.listarClases();
    }
}
