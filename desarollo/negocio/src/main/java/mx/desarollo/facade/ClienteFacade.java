package mx.desarollo.facade;

import mx.desarollo.delegate.ClienteDelegate;
import mx.desarollo.entity.Cliente;

public class ClienteFacade {

    private ClienteDelegate clienteDelegate = new ClienteDelegate(); // instancia del delegate

    public void registrarCliente(Cliente cliente) throws Exception { // metodo que usa el delegate para registrar
        clienteDelegate.registrarCliente(cliente); // delega la operacion al delegate
    }

    /*public Cliente obtenerCliente(int id) {
        return clienteDelegate.obtenerCliente(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDelegate.listarClientes();
    }

    public void eliminarCliente(int id) {
        clienteDelegate.eliminarCliente(id);
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        clienteDelegate.actualizarCliente(cliente);
    }*/
}
