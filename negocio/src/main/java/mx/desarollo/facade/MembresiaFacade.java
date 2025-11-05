package mx.desarollo.facade;

import mx.desarollo.delegate.MembresiaDelegate;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Membresia;

import java.util.List;

public class MembresiaFacade {
    private final MembresiaDelegate membresiaDelegate = new MembresiaDelegate();

    public void registrarMembresia(Membresia membresia, Cliente cliente) throws Exception {
        try {
            membresiaDelegate.registrarMembresia(membresia, cliente);
        } catch (Exception e) {
            throw new Exception("Error al registrar la membresia: " + e.getMessage());
        }
    }

    public boolean eliminarMembresia(String idMembresia) throws Exception {
        try{
            return membresiaDelegate.eliminarMembresia(idMembresia);
        } catch(Exception e){
            throw new Exception("Error al eliminar la membresia: " + e.getMessage());
        }
    }

    public void modificarMembresia(Membresia membresia) throws Exception {
        try{
            membresiaDelegate.modificarMembresia(membresia);
        } catch(Exception e){
            throw new Exception("Error al eliminar la membresia: " + e.getMessage());
        }
    }

    /**
     * Metodo para hacer busqueda por ID en las pagas, llamara a la instancia de PagaDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID
     * @Params Objeto de tipo String id
     * @return Una paga con id de la paga especificado
     */

    public Membresia obtenerMembresiaPorCliente(String idCliente, String membresia) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            return null;
        }
        return membresiaDelegate.obtenerMembresiaPorCliente(idCliente, membresia);
    }

    public List<Membresia> listarMembresias() throws Exception {
        return membresiaDelegate.listarMembresias();
    }
}
