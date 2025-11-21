package helper;
import java.io.Serializable;

import mx.desarollo.entity.Clase;
import mx.desarollo.entity.Cliente;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;

public class ClaseHelper implements Serializable {
    public void AltaClase(Clase cla) throws Exception {
        try {
            ServiceFacadeLocator.getInstanceClaseFacade().registrarClase(cla);
        } catch (Exception e) {
            throw new Exception("Error al registrar la clase: " + e.getMessage());
        }
    }

    public boolean eliminarClase(String idClase) throws Exception {
        return ServiceFacadeLocator.getInstanceClaseFacade().eliminarClase(idClase);
    }

    /**
     * Metodo para modificar los datos de una clase que llamara a la instancia de ClaseFacade
     * @Throws Si la base de datos rechaza la peticion de modificacion, ya sea por valor invalido
     * @Param Un objeto del tipo Clase
     * @return void
     */
    public void modificarClase(Clase cla) throws Exception {
        ServiceFacadeLocator.getInstanceClaseFacade().actualizarClase(cla);
    }

    /**
     * Metodo para obtener una clase por su ID que llamara a la instancia de ClaseFacade
     * @Throws Si la base de datos rechaza la peticion o no se encuentra la clase con el ID
     * @Param String id del cliente
     * @return Un objeto de tipo Clase
     */
    public Clase obtenerClase(String id) {
        return ServiceFacadeLocator.getInstanceClaseFacade().obtenerClasePorId(id);

    }

    public List<Clase> listarClases() {
        return ServiceFacadeLocator.getInstanceClaseFacade().listarClases();
    }

}
