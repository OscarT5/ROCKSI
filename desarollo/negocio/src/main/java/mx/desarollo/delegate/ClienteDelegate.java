package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Cliente;

import java.util.Date;

public class ClienteDelegate {

    private final ClienteDAO clienteDAO; // DAO usado por el delegate

    public ClienteDelegate() { // constructor del delegate
        this.clienteDAO = ServiceLocator.getInstanceClienteDAO(); // obtiene el DAP desde el service locator
    }

    public void registrarCliente(Cliente cliente) throws Exception { // metodo para validar y crear cliente
        //validar que el nombre no este vacio
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) { // si no hay nombre
            throw new Exception("el nombre no puede estar vacio."); // lanza excepcion con mensaje simple
        }

        //validar que el telefono no este vacio
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) { // si no hay telefono
            throw new Exception("el telefono no puede estar vacio."); // lanza excepcion con mensaje simple
        }

        //validar el formato del nombre
        if (!cliente.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) { // si el nombre contiene caracteres no permitidos
            throw new Exception("el nombre solo puede contener letras y espacios."); // lanza error indicando el formato
        }

        //validar el formato del telefono opcionalmente con + o espacios
        if (!cliente.getTelefono().matches("^[0-9]+$")) { // si el telefono no es solo numeros
            throw new Exception("el telefono solo puede contener numeros."); // lanza error indicando el formato
        }

        //registrar fecha y guardar
        cliente.setFechaRegistro(new Date()); // asigna la fecha actual como fecha de registro
        clienteDAO.crear(cliente); // llama al dao para persistir el cliente
    }


    /*public Cliente obtenerCliente(int id) {
        return clienteDAO.buscarPorId(id).orElse(null);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public void eliminarCliente(int id) {
        Cliente cliente = clienteDAO.find(id).orElse(null);
        if (cliente != null) {
            clienteDAO.eliminar(cliente);
        }
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }

        clienteDAO.actualizar(cliente);
    }*/
}