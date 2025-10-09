package mx.desarollo.facade;

import mx.desarollo.delegate.ClienteDelegate;
import mx.desarollo.entity.Cliente;

import java.util.List;

public class ClienteFacade {

    private ClienteDelegate clienteDelegate = new ClienteDelegate();

    public void registrarCliente(Cliente cliente) throws Exception {
        clienteDelegate.registrarCliente(cliente);
    }

    /*public Cliente obtenerCliente(int id) {
        return clienteDelegate.;
    }*/

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteDelegate
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @return Una lista de clientes que contendra todos los clientes de la base de datos
     */
    public List<Cliente> listarClientes() {
        return clienteDelegate.listarClientes();
    }

    public void eliminarCliente(int id) {
        clienteDelegate.eliminarCliente(id);
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        clienteDelegate.actualizarCliente(cliente);
    }
}
