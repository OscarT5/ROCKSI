package mx.desarollo.facade;

import mx.desarollo.delegate.PagaDelegate;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Item;
import mx.desarollo.entity.Membresia;
import mx.desarollo.entity.Paga;

import java.util.List;

public class PagaFacade {
    private final PagaDelegate pagaDelegate = new PagaDelegate();

    /**
     * Metodo para registrar una paga que llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion o hay algun objeto null
     * @Params Un objeto de tipo Paga y un objeto de tipo String id del Item
     * @return void
     */
    public void registrarPago(Paga paga, String idItem) throws Exception {
        try {
            pagaDelegate.registrarPago(paga, idItem);
        } catch (Exception e) {
            throw new Exception("Error al registrar el pago: " + e.getMessage());
        }
    }

    /**
     * Metodo para eliminar una paga que llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion o no se encuentra la paga con el ID
     * @Params Un objeto de tipo String id de la paga
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarPago(String idPago) throws Exception {
        try{
            return pagaDelegate.eliminarPago(idPago);
        } catch(Exception e){
            throw new Exception("Error al eliminar el pago: " + e.getMessage());
        }
    }

    /**
     * Metodo para obtnener una paga por su ID que llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID o no se encuentra la paga con ese ID
     * @Params Objeto de tipo String id de la paga
     * @return Un objeto de tipo Paga
     */
    public Paga obtenerPagaPorId(String id) {
        return pagaDelegate.obtenerPaga(id);
    }

    /**
     * Metodo para obtnener todas las pagas registradas que llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion de obtener todas las pagas
     * @return Una lista de pagas
     */
    public List<Paga> listarPagos() {
        return pagaDelegate.listarPagos();
    }

    /**
     * Metodo para buscar pagas por su ID que llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID o no se encuentran pagas con ese ID
     * @Params Objeto de tipo String idParcial (que sera la id de las pagas)
     * @return Una lista de pagas
     */
    public List<Paga> buscarPagosPorId(String idParcial) {
        return pagaDelegate.buscarPagosPorId(idParcial);
    }

}
