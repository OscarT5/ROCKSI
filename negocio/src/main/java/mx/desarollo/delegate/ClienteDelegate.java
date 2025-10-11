package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Cliente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        //cliente.setFechaRegistro(new Date());

        clienteDAO.crear(cliente);
    }

    /**
     * Metodo para hacer busqueda por ID en los clientes, llamara a la instancia de ClienteDAO
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID
     * @Params Objeto de tipo String id
     * @return Una lista con los clientes que cumplen con id del cliente especificado lladado resultado
     */
    public List<Cliente> obtenerClientePorId(String id) {
        List<Cliente> resultado = new ArrayList<>();
        clienteDAO.find(id).ifPresent(resultado::add);
        return resultado;
    }

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteDAO
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @return Una lista de clientes que contendra todos los clientes de la base de datos
     */
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
    }
}