package mx.desarollo.facade;

import mx.desarollo.delegate.ProductoDelegate;
import mx.desarollo.entity.Producto;

public class ProductoFacade {
    private final ProductoDelegate productoDelegate = new ProductoDelegate();

    public void altaProducto(Producto producto) throws Exception
    {
        try{
            productoDelegate.altaProducto(producto);
        } catch (Exception e){
            throw new Exception("Error al realizar la alta: " + e.getMessage());
        }
    }
}
