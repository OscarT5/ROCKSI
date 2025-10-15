package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Producto;

public class ProductoDelegate {

    private final ProductoDAO productoDAO;

    public ProductoDelegate() {
        this.productoDAO = ServiceLocator.getInstanceProductoDAO();
    }

    public void altaProducto(Producto producto) throws Exception {
        if (producto == null) {
            throw new Exception("El producto no puede ser nulo.");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }

        if (producto.getStock() == null || producto.getStock() <= 0) {
            throw new Exception("El stock debe ser mayor que cero.");
        }

        if (producto.getPrecio() <= 0) {
            throw new Exception("El precio debe ser mayor que cero.");
        }

        // Generar nuevo id
        producto.setIdProducto(productoDAO.generarNuevoIdProducto());

        // Guardar en la base de datos
        productoDAO.crear(producto);
    }
}
