package helper;
import java.io.Serializable;

import mx.desarollo.entity.Clase;
import mx.desarollo.entity.Cliente;
import mx.desarollo.integration.ServiceFacadeLocator;

    public class ClaseHelper implements Serializable {

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @Param Objeto de tipo Cliente
     * @return Una lista de clientes
     */
    public void ModificarClase(Clase cla) throws Exception {
        ServiceFacadeLocator.getInstanceClaseFacade().actualizarCliente(cla);
    }

    public Clase obtenerClase(String id) {
        return ServiceFacadeLocator.getInstanceClaseFacade().obtenerClasePorId(id);

    }

    }

