package mx.desarollo.dto;

//Este dto se utiliza para sacar todos los datos de los productos que se desplegaran en el reporte mensual
public class ProductoMensualDTO {
    private String nombre;
    private int cantidadVendida;
    private int cantidadActual;

    public ProductoMensualDTO(String nombre, int cantidadVendida, int cantidadActual) {
        this.nombre = nombre;
        this.cantidadVendida = cantidadVendida;
        this.cantidadActual = cantidadActual;
    }

    public String getNombre() { return nombre; }
    public String getCantidadVendida() { return String.valueOf(cantidadVendida); }
    public String getCantidadActual() { return String.valueOf(cantidadActual); }
}