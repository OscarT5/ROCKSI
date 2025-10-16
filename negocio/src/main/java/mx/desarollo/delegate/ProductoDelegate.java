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

        //genera un nuevo id
        producto.setIdItem(productoDAO.generarNuevoIdProducto());

        //Aqui se guarda en la BD
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
