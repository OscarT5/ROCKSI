package helper;

import mx.desarollo.entity.Paga;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;

public class PagaHelper implements Serializable {
    public void RealizarPago(Paga paga) throws Exception {
        try {
            ServiceFacadeLocator.getInstancePagaFacade().registrarPago(paga);
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

}
