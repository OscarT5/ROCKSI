package mx.desarollo.facade;

import mx.desarollo.delegate.ClaseDelegate;
import mx.desarollo.delegate.ClienteDelegate;
import mx.desarollo.entity.Clase;
import mx.desarollo.entity.Cliente;

import java.util.List;

public class ClaseFacade {

    private ClaseDelegate claseDelegate = new ClaseDelegate();

    /**
     * Metodo para hacer busqueda por ID en los clientes, llamara a la instancia de ClienteDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID
     * @Params Objeto de tipo String id
     * @return Una lista con los clientes que cumplen con id del cliente especificado
     */
    public void actualizarCliente(Clase cla) throws Exception {
        claseDelegate.actualizarClase(cla);
    }
    public Clase obtenerClasePorId(String id) {
        return claseDelegate.obtenerClase(id);
    }
}
