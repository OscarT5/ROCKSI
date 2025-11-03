package helper;

import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Membresia;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class MembresiaHelper implements Serializable {

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @Param Objeto de tipo Cliente
     * @return Una lista de clientes
     */
    public Membresia obtenerMembresiaPorCliente(String idCliente) {
        try {
            return ServiceFacadeLocator.getInstanceMembresiaFacade().obtenerMembresiaPorCliente(idCliente);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registrarMembresia(Membresia membresia, Cliente cliente) throws Exception {
        try {
            ServiceFacadeLocator.getInstanceMembresiaFacade().registrarMembresia(membresia, cliente);
        } catch (Exception e) {
            throw new Exception("Error al registrar la membresia: " + e.getMessage());
        }
    }


}
