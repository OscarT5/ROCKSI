package helper;
import java.io.Serializable;

import mx.desarollo.entity.Cliente;
import mx.desarollo.integration.ServiceFacadeLocator;

    /**
     * Metodo para hacer alta de un cliente que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza el registro
     * @Param Objeto de tipo Cliente
     */
    public class ClienteHelper implements Serializable {
    public void AltaCliente(Cliente cli) throws Exception {
        ServiceFacadeLocator.getInstanceClienteFacade().registrarCliente(cli);
    }
    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @Param Objeto de tipo Cliente
     */
    public void ObtenerClientes(Cliente cli) throws Exception {
        ServiceFacadeLocator.getInstanceClienteFacade().listarClientes();
    }

}
