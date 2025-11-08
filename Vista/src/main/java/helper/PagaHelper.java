package helper;

import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Item;
import mx.desarollo.entity.Membresia;
import mx.desarollo.entity.Paga;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;

public class PagaHelper implements Serializable {
    public void RealizarPago(Paga paga, String idItem) throws Exception {
        try {
            ServiceFacadeLocator.getInstancePagaFacade().registrarPago(paga, idItem);
        } catch (Exception e) {
            throw new Exception("Error al registrar el pago: " + e.getMessage());
        }
    }

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @Param Objeto de tipo Cliente
     * @return Una lista de clientes
     */
    public Paga obtenerPaga(String id) {
        return ServiceFacadeLocator.getInstancePagaFacade().obtenerPagaPorId(id);

    }

    public List<Paga> listarPagos() {
        return ServiceFacadeLocator.getInstancePagaFacade().listarPagos();
    }

    public List<Paga> buscarPagosPorId(String idParcial) {
        return ServiceFacadeLocator.getInstancePagaFacade().buscarPagosPorId(idParcial);
    }

}
