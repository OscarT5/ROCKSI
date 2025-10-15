package helper;

import mx.desarollo.entity.Producto;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class ProductoHelper implements Serializable {
    public void altaProducto(Producto producto) throws Exception {
        try{
            ServiceFacadeLocator.getInstanceProductoFacade().altaProducto(producto);
        }catch (Exception e){
            throw new Exception("Error al realizar la alta del producto" + e.getMessage());
        }
    }
}
