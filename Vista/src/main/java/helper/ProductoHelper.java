package helper;

import mx.desarollo.entity.Producto;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class ProductoHelper implements Serializable {

    public void modificarProducto(Producto p) throws Exception {
        ServiceFacadeLocator.getInstanceProductoFacade().actualizarProducto(p);
    }

    public Producto obtenerProducto(String id) {
        return ServiceFacadeLocator.getInstanceProductoFacade().obtenerProductoPorId(id);
    }
}
