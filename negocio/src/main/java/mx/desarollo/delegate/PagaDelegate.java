package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.PagaDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.*;

import java.time.LocalDate;
import java.util.List;

public class PagaDelegate {
    private final PagaDAO pagaDAO;

    public PagaDelegate() {
        this.pagaDAO = ServiceLocator.getInstancePagaDAO();
    }

    public void registrarPago(Paga paga, String tipo) throws Exception {
        if (paga.getIdCliente() == null || paga.getIdCliente().getIdCliente().trim().isEmpty()) {
            throw new Exception("Se debe ingresar el cliente al que se le cargo el pago");
        }
        if (paga.getMonto() == null) {
            throw new Exception("Se debe ingresar el monto del pago");
        }
        if (paga.getMonto() <= 0) {
            throw new Exception("El monto del pago debe ser mayor que 0");
        }
        if (paga.getPorPagar() < 0) {
            throw new Exception("El monto por pagar no debe ser menor que 0");
        }

        if ("membresia".equalsIgnoreCase(tipo)) {
            Item item = new Producto();
            item.setIdItem("CLI0002");
            paga.setIdItem(item);
            paga.setIdPaga(pagaDAO.generarNuevoIdPaga());
            pagaDAO.crear(paga);
        } else if ("clase".equalsIgnoreCase(tipo)) {
            Item item = new Clase();
            item.setIdItem("CLA1000");
            paga.setIdItem(item);
            paga.setIdPaga(pagaDAO.generarNuevoIdPaga());
            pagaDAO.crear(paga);
        }
    }

    public boolean eliminarPago(String id) throws Exception {
        if(id == null || id.trim().isEmpty()) {
            throw new Exception("El id del pago esta vacio");
        }
        return pagaDAO.eliminarPaga(id);
    }

    public Paga obtenerPaga(String id) {
        try {
            if (id == null) return null;
            id = id.trim();
            if (id.isEmpty()) return null;

            Paga paga = pagaDAO.find(id).orElse(null);

            return paga;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo el pago con id=" + id, e);
        }
    }

    public List<Paga> listarPagos() {
        return pagaDAO.findAllWithPaga();
    }

}
