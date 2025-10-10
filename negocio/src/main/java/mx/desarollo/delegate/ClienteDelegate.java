package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Cliente;
import java.util.Date;

public class ClienteDelegate {

    private final ClienteDAO clienteDAO;

    public ClienteDelegate() {
        this.clienteDAO = ServiceLocator.getInstanceClienteDAO();
    }

    public void registrarCliente(Cliente cliente) throws Exception {
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("El telefono no puede estar vacio.");
        }

        cliente.setFechaRegistro(new Date());
        clienteDAO.save(cliente);
    }

    //Metodo para eliminar el cliente
    public boolean eliminarCliente(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new Exception("El id del cliente esta vacio");
        }

        return clienteDAO.eliminarCliente(idCliente);
    }
}
