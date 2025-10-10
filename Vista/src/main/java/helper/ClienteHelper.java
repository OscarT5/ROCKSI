package helper;

import java.io.Serializable;
import mx.desarollo.entity.Cliente;
import mx.desarollo.integration.ServiceFacadeLocator;

public class ClienteHelper implements Serializable {

    public void AltaCliente(Cliente cli) throws Exception {
        ServiceFacadeLocator.getInstanceClienteFacade().registrarCliente(cli);
    }

    public boolean eliminarCliente(String idCliente) throws Exception {
        return ServiceFacadeLocator.getInstanceClienteFacade().eliminarCliente(idCliente);
    }

    //Codigo de prueba para eliminar a un cliente
    /*
    public void pruebaEliminarCliente() {
        try {
            String id = "123";
            boolean eliminado = eliminarCliente(id);
            if (eliminado) {
                System.out.println("Cliente eliminado correctamente");
            } else {
                System.out.println("No se encontro cliente con ese ID");
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
     */
}
