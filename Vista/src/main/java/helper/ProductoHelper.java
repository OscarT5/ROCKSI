package helper;

import mx.desarollo.entity.Producto;
import mx.desarollo.integration.ServiceFacadeLocator;
import java.util.List;
import java.io.Serializable;

public class ProductoHelper implements Serializable {
    public void altaProducto(Producto producto) throws Exception {
        try{
            ServiceFacadeLocator.getInstanceProductoFacade().altaProducto(producto);
        }catch (Exception e){
            throw new Exception("Error al realizar la alta del producto" + e.getMessage());
        }
    }

    public boolean eliminarProductos(String idProducto) throws Exception {
        return ServiceFacadeLocator.getInstanceProductoFacade().eliminarProducto(idProducto);
    }

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteFacade
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @Param Objeto de tipo Cliente
     * @return Una lista de clientes
     */
    public void modificarProducto(Producto producto) throws Exception {
        ServiceFacadeLocator.getInstanceProductoFacade().actualizarProducto(producto);
    }


    public Producto obtenerProducto(String id) {
        return ServiceFacadeLocator.getInstanceProductoFacade().obtenerProductoPorId(id);

    }

    public List<Producto> listarProducto() throws Exception {
        return ServiceFacadeLocator.getInstanceProductoFacade().listarProductos();
    }
}
