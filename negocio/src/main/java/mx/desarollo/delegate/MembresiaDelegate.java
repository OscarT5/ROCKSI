package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.MembresiaDAO;
import mx.avanti.desarollo.dao.PagaDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MembresiaDelegate {
    private final MembresiaDAO membresiaDAO;

    public MembresiaDelegate() {
        this.membresiaDAO = ServiceLocator.getInstanceMembresiaDAO();
    }

    public void registrarMembresia(Membresia membresia, Cliente cliente) throws Exception {
        if (cliente == null || cliente.getIdCliente() == null || cliente.getIdCliente().trim().isEmpty()) {
            throw new Exception("Se debe ingresar el cliente al que se le asignó la membresía");
        }

        String nuevoId = Membresia.generarNuevoId();
        membresia.setIdItem(nuevoId);
        membresia.setIdUsuarioAdmin("ADM1000");
        membresia.setIdCliente(cliente);
        membresia.setFechaVencimiento(LocalDate.now().plusDays(30));

        membresiaDAO.save(membresia);
    }



    public boolean eliminarMembresia(String idMembresia) throws Exception {
        if(idMembresia == null || idMembresia.trim().isEmpty()) {
            throw new Exception("El id de la membresia esta vacio");
        }
        return membresiaDAO.eliminarMembresia(idMembresia);
    }

    /*public Membresia obtenerMembresia(String idMembresia) throws Exception {
        try {
            if (idMembresia == null) return null;
            idMembresia = idMembresia.trim();
            if (idMembresia.isEmpty()) return null;

            Membresia membresia = membresiaDAO.find(idMembresia).orElse(null);

            return membresia;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo la membresia con id=" + idMembresia, e);
        }
    }*/

    public Membresia obtenerMembresiaPorCliente(String idCliente) throws Exception {
        try {
            if (idCliente == null) return null;
            idCliente = idCliente.trim();
            if (idCliente.isEmpty()) return null;

            Membresia membresia = membresiaDAO.obtenerMembresiaPorCliente(idCliente);

            return membresia;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo la membresia por cliente con id=" + idCliente, e);
        }
    }

    public List<Membresia> listarMembresias() {
        return membresiaDAO.findAllWithMembresia();
    }

}
