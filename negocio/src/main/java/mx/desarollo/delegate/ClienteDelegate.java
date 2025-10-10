package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Membresia;

import java.util.Date;
import java.util.List;

public class ClienteDelegate {

    private final ClienteDAO clienteDAO;

    public ClienteDelegate() {
        this.clienteDAO = ServiceLocator.getInstanceClienteDAO();
    }

    public void registrarCliente(Cliente cliente) throws Exception {
        //validar que el nombre no este vacio
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("el nombre no puede estar vacio.");
        }

        //validar que el telefono no este vacio
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("el telefono no puede estar vacio.");
        }

        // trim inicial
        String rawTelefono = cliente.getTelefono().trim();

        // remueve espacios y guiones
        String telefonoNormalizado = rawTelefono.replaceAll("[\\s\\-()]", "");

        //validar el formato del nombre
        if (!cliente.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new Exception("el nombre solo puede contener letras y espacios.");
        }

        //validar el formato del telefono
        String normal = rawTelefono.replaceAll("[^0-9]", "");
        if (!normal.matches("\\d{7,15}")) {
            throw new Exception("Telefono inválido. Debe contener entre 7 y 15 dígitos.");
        }
        cliente.setTelefono(normal);

        //busqueda de membresia simulada inyectada a la bd para probar alta de clientes
        Membresia membresiaPorDefecto = clienteDAO.getEntityManager().find(Membresia.class, "M002");
        if (membresiaPorDefecto == null) {
            throw new RuntimeException("No existe la membresía por defecto en la BD");
        }
        cliente.setMembresia(membresiaPorDefecto);

        //registrar fecha y guardar
        cliente.setFechaRegistro(new Date());
        clienteDAO.crear(cliente);
    }

    /*public Cliente obtenerCliente(int id) {
        return clienteDAO.find(id).orElse(null);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }

    public void eliminarCliente(int id) {
        Cliente cliente = clienteDAO.find(id).orElse(null);
        if (cliente != null) {
            clienteDAO.delete(cliente);
        }
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }

        clienteDAO.update(cliente);
    }*/
}