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

        if (producto.getPrecio() <= 0) {
            throw new Exception("El precio debe ser mayor que cero.");
        }

        if (producto.getProveedor() == null || producto.getProveedor().trim().isEmpty()) {
            throw new Exception("El proveedor no puede estar vacío.");
        }

        //genera un nuevo id
        producto.setIdItem(productoDAO.generarNuevoIdProducto());

        //Aqui se guarda en la BD
        productoDAO.crear(producto);
    }

    public void reducirStock(String idProducto) throws Exception {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new Exception("El ID del producto no puede estar vacío.");
        }

        Producto producto = productoDAO.buscarProductoPorId(idProducto);
        if (producto == null) {
            throw new Exception("No existe un producto con el ID: " + idProducto);
        }

        if (producto.getStock() <= 0) {
            throw new Exception("El producto " + producto.getNombre() + " no tiene stock disponible.");
        }

        productoDAO.reducirStock(idProducto);
    }


    /**
     * Metodo para eliminar un producto por su ID que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion, si no se encuentra un producto con el ID o si la cadena esta vacia
     * @params Un objeto de tipo String id
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarProducto(String idProducto) throws Exception {
        // Si el String idProducto esta vacio entonces
        if(idProducto == null || idProducto.trim().isEmpty()) {
            throw new Exception("El id del producto esta vacio");
        }
        // Si no entonces retorna la respuesta que entrega el metodo .eliminarProducto(idProducto)
        return productoDAO.eliminarProducto(idProducto);
    }

    /**
     * Metodo para modificar los datos de un producto que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion o si el producto es null
     * @params Un objeto de tipo Producto
     * @return void
     */
    public void actualizarProducto(Producto producto) throws Exception {
        String idAActualizar = producto.getIdItem();
        // Si el idActualizar esta vacio entonces
        if (idAActualizar == null || idAActualizar.trim().isEmpty()) {
            throw new Exception("No se proporcionó ID de producto para modificar.");
        }

        // Creo una instancia de Producto llamada existente al que le asigno el prodcuto que me retorne el metodo .buscarProductoPorId()
        Producto existente = productoDAO.buscarProductoPorId(idAActualizar);

        // Si existente (Producto) es null
        if (existente == null) {
            throw new Exception("No existe el producto con ID " + idAActualizar + " en la base de datos.");
        }

        // Validaciones: Nombre vacio
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        // Que el Stock no sea negativo
        if (producto.getStock() < 0) {
            throw new Exception("El Stock debe ser numeros positivos.");
        }
        // Que el precio sea mayor que cero
        if (producto.getPrecio() <= 0) {
            throw new Exception("El precio debe ser mayor que cero.");
        }
        // Que el provedor no este vacio
        if (producto.getProveedor() == null || producto.getProveedor().trim().isEmpty()) {
            throw new Exception("El proveedor no puede estar vacío.");
        }

        // Aplicar cambios
        existente.setNombre(producto.getNombre());
        existente.setStock(producto.getStock());
        existente.setPrecio(producto.getPrecio());
        existente.setProveedor(producto.getProveedor());

        // Modifico el producto con el metodo de la instancia productoDAO .actualizarProducto() y entre parametros el objeto de producto
        productoDAO.actualizarProducto(existente);
    }

    /**
     * Metodo para obtener un producto por su ID que llamara a la instancia de ProductoDAO
     * @Throws Si la base de datos rechaza la peticion o no se encuentra un producto con el ID
     * @params Un objeto de tipo String id
     * @return Un objeto de tipo Producto
     */
    public Producto obtenerProducto(String id) {
        try {
            // Si el String id es null entonces
            if (id == null) return null;
            id = id.trim();
            // Si el String id esta vacio
            if (id.isEmpty()) return null;

            // Si la String id si contiene algo entonces, busca con el metodo de la instancia del DAO .buscarProductoPorId() y lo que retorne se lo asigna a un objeto de tipo Producto
            Producto producto = productoDAO.buscarProductoPorId(id);

            // Retorno el producto encontrado
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
        return productoDAO.listarTodosLosProductos();
    }
}
