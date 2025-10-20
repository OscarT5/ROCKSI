package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Producto;

import java.math.BigDecimal;
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


        //genera un nuevo id
        producto.setIdItem(productoDAO.generarNuevoIdProducto());

        //Aqui se guarda en la BD
        productoDAO.crear(producto);
    }

    /**
     * Metodo para eliminar un producto por su ID que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion, si no se encuentra un producto con el ID o si la cadena esta vacia
     * @params Un objeto de tipo String id
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarProducto(String idProducto) throws Exception {
        if(idProducto == null || idProducto.trim().isEmpty()) {
            throw new Exception("El id del producto esta vacio");
        }
        return productoDAO.eliminarProducto(idProducto);
    }

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

    //Actualiza los datos de un producto existente
    /*public void actualizarProducto(Producto p) throws Exception {
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
    }*/

    /**
     * Metodo para obtener un producto por su ID que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion o no se encuentra un producto con el ID
     * @params Un objeto de tipo String id
     * @return Un objeto de tipo Producto
     */
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

    /**
     * Metodo para listar todos los productos registrados que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion de selec * from tabla
     * @return Una lista de productos que contendra todos los productos de la base de datos
     */
    public List<Producto> listarProductos() {
        return productoDAO.findAll();
    }
}
