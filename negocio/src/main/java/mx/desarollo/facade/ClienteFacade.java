package mx.desarollo.facade;

import mx.desarollo.delegate.ClienteDelegate;
import mx.desarollo.entity.Cliente;

public class ClienteFacade {

    private final ClienteDelegate clienteDelegate = new ClienteDelegate();

    public void registrarCliente(Cliente cliente) throws Exception {
        clienteDelegate.registrarCliente(cliente);
    }

    public boolean eliminarCliente(String idCliente) throws Exception {
        return clienteDelegate.eliminarCliente(idCliente);
    }
}
