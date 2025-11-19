package helper;

import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Item;
import mx.desarollo.entity.Membresia;
import mx.desarollo.entity.Paga;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;

public class PagaHelper implements Serializable {

    /**
     * Metodo para realizar un pago que llamara a la instancia de pagaFacade
     * @Throws Si la base de datos rechaza la peticion a la hora de guardarlo
     * @Param Un objeto de tipo Paga y un objeto String id del Item
     * @return void
     */
    public void RealizarPago(Paga paga, String idItem) throws Exception {
        try {
            ServiceFacadeLocator.getInstancePagaFacade().registrarPago(paga, idItem);
        } catch (Exception e) {
            throw new Exception("Error al registrar el pago: " + e.getMessage());
        }
    }

    /**
     * Metodo para obtener una paga por su ID que llamara a la instancia de pagaFacade
     * @Throws Si la base de datos rechaza la peticion o no se encuntra la paga con el ID
     * @Param Un objeto String id de la paga
     * @return Un objeto de tipo Paga
     */
    public Paga obtenerPaga(String id) {
        return ServiceFacadeLocator.getInstancePagaFacade().obtenerPagaPorId(id);

    }

    /**
     * Metodo para obtener todas las pagas registradas en la base de datos que llamara a la instancia de pagaFacade
     * @Throws Si la base de datos rechaza la peticion de obtener todos las pagas
     * @return Una Lista de pagas
     */
    public List<Paga> listarPagos() {
        return ServiceFacadeLocator.getInstancePagaFacade().listarPagos();
    }

    /**
     * Metodo para buscar una paga por su ID que llamara a la instancia de pagaFacade
     * @Throws Si la base de datos rechaza la peticion o no se encuntra la paga con el ID
     * @Param Un objeto String idParcial de la paga
     * @return Una Lista de pagas
     */
    public List<Paga> buscarPagosPorId(String idParcial) {
        return ServiceFacadeLocator.getInstancePagaFacade().buscarPagosPorId(idParcial);
    }

}
