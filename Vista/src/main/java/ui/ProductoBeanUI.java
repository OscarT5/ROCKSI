package ui;

import helper.ClaseHelper;
import helper.ProductoHelper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarollo.entity.Producto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Named("productoBeanUI")
@ViewScoped
public class ProductoBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;
    private final ProductoHelper productoHelper = new ProductoHelper();

    private List<Producto> listaProductos; // lista filtrada que se mostrara en la UI
    private List<Producto> originalProductos; // copia completa
    private String filtro; // texto del filtro
    private Producto productoSeleccionado;

    // Constructor
    public ProductoBeanUI() {
        this.listaProductos = new ArrayList<>();
        this.originalProductos = new ArrayList<>();
    }

    // Metodo en cadena despues de contruir
    @PostConstruct
    public void init() { cargarProductos();
    }


    /**
     * Metodo para cargar los productos registrados en la base de datos que llamara a la instancia de productoHelper
     * @Throws Si la base de datos rechaza la peticion
     * @Params ninguno
     * @return void
     */
    public void cargarProductos() {
        try {
            // Guardo en una lista de productos llamada listaObtenida = la lista de productos que me retornara el metodo .listarProductos()
            List<Producto> listaObtenida = productoHelper.listarProductos(); // debe implementar listarProductos()
            // Si la listaObtenida esta vacia
            if (listaObtenida == null) {
                 listaObtenida = new ArrayList<>(); // Entonces crea una completamente nueva
            }
            // Copia para evitar el error ConcurrentModification
            this.originalProductos = new ArrayList<>(listaObtenida);
            this.listaProductos = new ArrayList<>(listaObtenida);
        } catch (Exception e) {
            e.printStackTrace();
            this.originalProductos = new ArrayList<>();
            this.listaProductos = new ArrayList<>();
        }
    }

    /**
     * Metodo para cargar los productos registrados en la base de datos que llamara a la instancia de productoHelper
     * @Throws Si la base de datos rechaza la peticion
     * @Params ninguno
     * @return void
     */
    //este metodo sirve para la busqueda por nombres o id
    public void filtrarPorId() {
        try {
            if (filtro == null || filtro.isEmpty()) {
                listaProductos = productoHelper.listarProductos();
            } else {
                Producto p = productoHelper.obtenerProducto(filtro);
                listaProductos = new ArrayList<>();
                if (p != null) {
                    listaProductos.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para recargar los productos registrados en la tabla de consulta que llama al metodo cargarProductos()
     * @Throws Si la base de datos rechaza la peticion de cargarProductos()
     * @Params ninguno
     * @return void
     */
    public void recargar() {
        cargarProductos();
    }

    // Getters y Setters
    public List<Producto> getListaProductos() { return listaProductos; }
    public void setListaClases(List<Producto> lista) {
        this.listaProductos = lista;
    }

    public String getFiltro() {
        return filtro;
    }
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Producto getProductoSeleccionado() {return productoSeleccionado;}
    public void setProductoSeleccionado(Producto productoSeleccionado) {this.productoSeleccionado = productoSeleccionado;}

}
