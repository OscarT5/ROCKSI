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

    /**
     * Metodo para registrar un pago que llamara a la instancia de pagaDAO
     * @Throws Si la base de datos rechaza la peticion o alguna variable o objeto es null
     * @params Un objeto de tipo Paga y un objeto String id del Item
     * @return void
     */
    public void registrarPago(Paga paga, String idItem) throws Exception {
        // Si el id del pago es null o esta vacio, entonces
        if (paga.getIdCliente() == null || paga.getIdCliente().getIdCliente().trim().isEmpty()) {
            throw new Exception("Se debe ingresar el cliente al que se le cargo el pago");
        }

        // Si el id del Item es null o esta vacio, entonces
        if (idItem == null || idItem.trim().isEmpty()) {
            throw new Exception("Se debe especificar un ID de Item válido");
        }

        // Si el monto es negativo entonces
        if (paga.getPorPagar() < 0) {
            throw new Exception("El monto por pagar no debe ser menor que 0");
        }

        // Si todo esta bien, busca el item con el metodo .findItemById() y lo guarda en una variable auxiliar de tipo Item
        Item item = pagaDAO.findItemById(idItem);

        // Si el item es null entonces
        if (item == null) {
            throw new Exception("El Item con ID " + idItem + " no existe en la base de datos.");
        }

        // Se le asigna el item al pago
        paga.setIdItem(item);

        // Se genera un nuevo id para el pago
        paga.setIdPaga(pagaDAO.generarNuevoIdPaga());

        // Se crea el pago con el metodo .crear() del pagaDAO
        pagaDAO.crear(paga);
    }

    /**
     * Metodo para eliminar un pago que llamara a la instancia de pagaDAO
     * @Throws Si la base de datos rechaza la peticion o no se encontro una paga con el ID
     * @params Un objeto String id de la paga
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarPago(String id) throws Exception {
        // si la String id es null o esta vacio entonces
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El id del pago esta vacio");
        }
        // Si la String id contiene un id, elimina la paga con el id y retorna la respuesta boolean
        return pagaDAO.eliminarPaga(id);
    }

    /**
     * Metodo para obtener una paga por su ID que llamara a la instancia de pagaDAO
     * @Throws Si la base de datos rechaza la peticion o no se encontro una paga con el ID
     * @params Un objeto String id de la paga
     * @return Un objeto de tipo Paga
     */
    public Paga obtenerPaga(String id) {
        try {
            // Si la String id es null entonces retorna null
            if (id == null) return null;
            id = id.trim();
            // Si la String id esta vacio entonces retorna null
            if (id.isEmpty()) return null;

            // Si la String id contiene algo busca la paga por su id con el metodo .find() del pagaDAO y lo guardo en una variable de tipo Paga
            Paga paga = pagaDAO.find(id).orElse(null);

            // Si encontro la paga, retorna la paga
            return paga;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo el pago con id=" + id, e);
        }
    }

    /**
     * Metodo para obtener todos los pagos registrados que llamara a la instancia de pagaDAO
     * @Throws Si la base de datos rechaza la peticion de obtener todas las pagas
     * @return Una lista de pagas
     */
    public List<Paga> listarPagos() {
        return pagaDAO.findAllWithPaga();
    }

    /**
     * Metodo para buscar pagas por su ID que llamara a la instancia de pagaDAO
     * @Throws Si la base de datos rechaza la peticion o no se encuentran pagas con el ID
     * @params Un objeto String idParcial (id de la Paga)
     * @return Una lista de pagas
     */
    public List<Paga> buscarPagosPorId(String idParcial) {
        return pagaDAO.buscarPagosPorId(idParcial);
    }

}
