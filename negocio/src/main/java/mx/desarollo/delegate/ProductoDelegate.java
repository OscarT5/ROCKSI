package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Producto;

import java.util.List;

public class ProductoDelegate {
    private final ProductoDAO productoDAO;

    public ProductoDelegate() {
        this.productoDAO = ServiceLocator.getInstanceProductoDAO();
    }

    public void registrarProducto(Producto producto) throws Exception {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto esta vacio");
        }
        if (producto.getStock() <= 0) {
            throw new Exception("El Stock debe ser mayor que cero");
        }

        //Se llama al metodo para asignar y crear un nuevoID
        producto.setIdItem(productoDAO.generarNuevoIdProducto());

        productoDAO.crear(producto);
    }

    public boolean eliminarProducto(String idProducto) throws Exception {
        if(idProducto == null || idProducto.trim().isEmpty()) {
            throw new Exception("El id del producto esta vacio");
        }
        return productoDAO.eliminarProducto(idProducto);
    }

    /**
     * Metodo para hacer consulta de todos los clientes que llamara a la instancia de ClienteDAO
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @return Una lista de clientes que contendra todos los clientes de la base de datos
     */

    /*
    este metodo sirve para actualizar clientes mediante su ID
    que esta es proporcionada por el bean, falta implementar el bean
     */
    public void actualizarProducto(Producto producto) throws Exception {
        String idAActualizar = producto.getIdItem();
        if (idAActualizar == null || idAActualizar.trim().isEmpty()) {
            throw new Exception("No se proporcionó ID de producto para modificar.");
        }

        Producto existente = productoDAO.buscarProductoPorId(idAActualizar);
        if (existente == null) {
            throw new Exception("No existe el producto con ID " + idAActualizar + " en la base de datos.");
        }

        // Validaciones
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        if (producto.getStock() <= 0) {
            throw new Exception("El Stock debe ser mayor que cero.");
        }

        // Aplicar cambios
        existente.setNombre(producto.getNombre());
        existente.setStock(producto.getStock());

        productoDAO.actualizarProducto(existente);
    }


    public Producto obtenerProducto(String id) {
        try {
            if (id == null) return null;
            id = id.trim();
            if (id.isEmpty()) return null;

            Producto producto = productoDAO.find(id).orElse(null);

            return producto;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo el producto con id=" + id, e);
        }
    }

    public List<Producto> listarProductos() {
        return productoDAO.findAll();
    }
}
