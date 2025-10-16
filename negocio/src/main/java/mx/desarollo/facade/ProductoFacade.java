package mx.desarollo.facade;

import mx.desarollo.delegate.ProductoDelegate;
import mx.desarollo.entity.Producto;

public class ProductoFacade {

    private final ProductoDelegate delegate = new ProductoDelegate();

    public void actualizarProducto(Producto p) throws Exception {
        delegate.actualizarProducto(p);
    }

    public Producto obtenerProductoPorId(String id) {
        return delegate.obtenerProducto(id);
    }
}
