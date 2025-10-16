package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Producto;

import java.math.BigDecimal;

public class ProductoDelegate {

    private final ProductoDAO productoDAO;

    public ProductoDelegate() {
        this.productoDAO = ServiceLocator.getInstanceProductoDAO();
    }

    //Actualiza los datos de un producto existente
    public void actualizarProducto(Producto p) throws Exception {
        if (p == null || p.getIdProducto() == null || p.getIdProducto().trim().isEmpty()) {
            throw new Exception("Producto inválido para actualización: ID nulo o vacío.");
        }

        // Buscar producto existente por su ID
        Producto existente = productoDAO.buscarProductoPorId(p.getIdProducto());
        if (existente == null) {
            throw new Exception("No existe ningún producto con el ID: " + p.getIdProducto());
        }

        // Validaciones de datos
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        if (p.getStock() == null || p.getStock() < 0) {
            throw new Exception("El stock debe ser 0 o mayor.");
        }
        if (p.getPrecio() < 0) {
            throw new Exception("El precio debe ser mayor o igual a 0.");
        }

        //asignar los nuevos valores
        existente.setNombre(p.getNombre().trim());
        existente.setStock(p.getStock());
        existente.setPrecio(p.getPrecio());

        //sincronizar precio con el item relacionado
        if (existente.getItem() != null) {
            try {
                existente.getItem().setPrecio(BigDecimal.valueOf(p.getPrecio()));
            } catch (Exception ignored) {
                System.out.println("Advertencia: no se pudo sincronizar el precio con Item.");
            }
        }

        productoDAO.actualizarProducto(existente);
    }

    public Producto obtenerProducto(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return productoDAO.find(id.trim()).orElse(null);
    }
}
