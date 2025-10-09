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
    public void eliminarCliente(String idCliente) throws Exception {
        ServiceFacadeLocator.getInstanceClienteFacade().eliminarCliente(idCliente);
    }

    //COdigo de prueba para eliminar un cliente

    public void pruebaEliminarCliente() {
        try {
            String id = "123";
            eliminarCliente(id);
            System.out.println("Cliente eliminado");
        } catch (Exception e) {
            System.err.println("rror al eliminar cliente: " +e.getMessage());
            e.printStackTrace();
        }
    }

}
