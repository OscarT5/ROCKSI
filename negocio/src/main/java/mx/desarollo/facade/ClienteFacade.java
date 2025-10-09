package mx.desarollo.facade;

import mx.desarollo.delegate.ClienteDelegate;
import mx.desarollo.entity.Cliente;
import java.util.List;

public class ClienteFacade {

    private ClienteDelegate clienteDelegate = new ClienteDelegate();

    public void registrarCliente(Cliente cliente) throws Exception {
        clienteDelegate.registrarCliente(cliente);
    }

    public void eliminarCliente(String idCliente) throws Exception {
        clienteDelegate.eliminarCliente(idCliente);
    }
}
