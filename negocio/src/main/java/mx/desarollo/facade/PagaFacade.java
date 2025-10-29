package mx.desarollo.facade;

import mx.desarollo.delegate.PagaDelegate;
import mx.desarollo.entity.Paga;

import java.util.List;

public class PagaFacade {
    private final PagaDelegate pagaDelegate = new PagaDelegate();

    public void registrarPago(Paga paga) throws Exception {
        try {
            pagaDelegate.registrarPago(paga);
        } catch (Exception e) {
            throw new Exception("Error al registrar el pago: " + e.getMessage());
        }
    }

    public boolean eliminarPago(String idPago) throws Exception {
        try{
            return pagaDelegate.eliminarPago(idPago);
        } catch(Exception e){
            throw new Exception("Error al eliminar el pago: " + e.getMessage());
        }
    }

    /**
     * Metodo para hacer busqueda por ID en las pagas, llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID
     * @Params Objeto de tipo String id
     * @return Una paga con id de la paga especificado
     */
    public Paga obtenerPagaPorId(String id) {
        return pagaDelegate.obtenerPaga(id);
    }

    public List<Paga> listarPagos() {
        return pagaDelegate.listarPagos();
    }
}
