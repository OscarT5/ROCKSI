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
    private static final String ID_ITEM_RETIRO = "RC1000";

    public void registrarPago(Paga paga, String idItem) throws Exception {
        if (paga.getIdCliente() == null || paga.getIdCliente().getIdCliente().trim().isEmpty()) {
            throw new Exception("Se debe ingresar el cliente al que se le cargo el pago");
        }
        if (idItem == null || idItem.trim().isEmpty()) {
            throw new Exception("Se debe especificar un ID de Item válido");
        }
        if (paga.getPorPagar() < 0) {
            throw new Exception("El monto por pagar no debe ser menor que 0");
        }

        Item item = pagaDAO.findItemById(idItem);

        if (item == null) {
            throw new Exception("El Item con ID " + idItem + " no existe en la base de datos.");
        }

        paga.setIdItem(item);

        paga.setIdPaga(pagaDAO.generarNuevoIdPaga());

        pagaDAO.crear(paga);
    }

    public boolean eliminarPago(String id) throws Exception {
        if (id == null || id.trim().isEmpty()) {
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

    public List<Paga> buscarPagosPorId(String idParcial) {
        return pagaDAO.buscarPagosPorId(idParcial);
    }

}
