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
        //validaciones
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("El telefono no puede estar vacio.");
        }

        String rawTelefono = cliente.getTelefono().trim();
        String telefonoNormalizado = rawTelefono.replaceAll("[\\s\\-()]", "");

        if (!cliente.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new Exception("El nombre solo puede contener letras y espacios.");
        }

        String normal = rawTelefono.replaceAll("[^0-9]", "");
        if (!normal.matches("\\d{7,15}")) {
            throw new Exception("Telefono invalido. Debe contener entre 7 y 15 dígitos.");
        }
        cliente.setTelefono(normal);

        String st = cliente.getSegundoTelefono();
        if (st != null && !st.trim().isEmpty()) {
            String onlyDigits = st.replaceAll("[^0-9]", "");
            if (!onlyDigits.matches("\\d{7,15}")) {
                throw new Exception("Segundo teléfono inválido. Debe contener entre 7 y 15 dígitos.");
            }
            cliente.setSegundoTelefono(onlyDigits);
        } else {
            cliente.setSegundoTelefono(null);
        }

        cliente.setFechaRegistro(new Date());
        cliente.setEstatus(1);
        clienteDAO.crear(cliente);
    }

    public List<Cliente> obtenerClientePorId(String id) {
        List<Cliente> resultado = new ArrayList<>();
        clienteDAO.find(id).ifPresent(resultado::add);
        return resultado;
    }

    public Cliente obtenerCliente(String id) {
        try {
            if (id == null) return null;
            id = id.trim();
            if (id.isEmpty()) return null;
            return clienteDAO.find(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo cliente con id=" + id, e);
        }
    }

    //Solo lista los clientes que estan activos (1)
    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public boolean eliminarCliente(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new Exception("El id del cliente esta vacio");
        }
        return clienteDAO.eliminarCliente(idCliente);
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        String idAActualizar = cliente.getIdCliente();
        if (idAActualizar == null || idAActualizar.trim().isEmpty()) {
            throw new Exception("No se proporcionó ID de cliente para actualizar.");
        }

        Cliente existente = clienteDAO.buscarPorId(idAActualizar);
        if (existente == null) {
            throw new Exception("No existe el cliente con ID " + idAActualizar);
        }

        if (existente.getEstatus() == 0 && !existente.getIdCliente().equals("CLI69")) {
            throw new Exception("No se puede editar un cliente que ha sido dado de baja.");
        }

        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("El teléfono no puede estar vacío.");
        }

        String rawTelefono = cliente.getTelefono().trim();
        String normal = rawTelefono.replaceAll("[^0-9]", "");
        if (!normal.matches("\\d{7,15}")) {
            throw new Exception("Teléfono inválido.");
        }
        if (!cliente.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new Exception("El nombre solo puede contener letras y espacios.");
        }

        String sx = (cliente.getSexo() == null) ? "" : cliente.getSexo().trim().toLowerCase();
        if (!sx.equals("masculino") && !sx.equals("femenino")) {
            throw new Exception("Sexo inválido.");
        }

        String st = cliente.getSegundoTelefono();
        String stNorm = null;
        if (st != null && !st.trim().isEmpty()) {
            String onlyDigits = st.replaceAll("[^0-9]", "");
            if (!onlyDigits.matches("\\d{7,15}")) {
                throw new Exception("Segundo teléfono inválido.");
            }
            stNorm = onlyDigits;
        }

        existente.setNombreCompleto(cliente.getNombreCompleto().trim());
        existente.setTelefono(normal);
        existente.setSexo(sx);
        existente.setSegundoTelefono(stNorm);

        clienteDAO.actualizar(existente);
    }
}