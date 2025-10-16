package mx.desarollo.facade;

import mx.desarollo.delegate.ProductoDelegate;
import mx.desarollo.entity.Producto;

import java.util.List;

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
    public boolean eliminarProducto(String idProducto) throws Exception {
        try{
            return productoDelegate.eliminarProducto(idProducto);
        } catch(Exception e){
            throw new Exception("Error al eliminar el producto: " + e.getMessage());
        }
    }

    /**
     * Metodo para hacer busqueda por ID en los clientes, llamara a la instancia de ClienteDelegate
     * @Throws Si la base de datos rechaza la peticion de busqueda por ID
     * @Params Objeto de tipo String id
     * @return Una lista con los clientes que cumplen con id del cliente especificado
     */
    public void actualizarProducto(Producto producto) throws Exception {
        productoDelegate.actualizarProducto(producto);
    }

    public Producto obtenerProductoPorId(String id) {
        return productoDelegate.obtenerProducto(id);
    }

    public List<Producto> listarProductos() {
        return productoDelegate.listarProductos();
    }
}
