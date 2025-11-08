package mx.desarollo.dto;

//Data transfer object para pasar datos de producto al pdf que se va a generar
public class ProductoReporteDTO {
    String nombre;
    int stockInicial;
    int cantidadVendida;
    int stockFinal;

    public ProductoReporteDTO(String nombre, int stockInicial, int cantidadVendida, int stockFinal) {
        this.nombre = nombre;
        this.stockInicial = stockInicial;
        this.cantidadVendida = cantidadVendida;
        this.stockFinal = stockFinal;
    }

    public String getNombre() { return nombre; }
    public String getStockInicial() { return String.valueOf(stockInicial); }
    public String getCantidadVendida() { return String.valueOf(cantidadVendida); }
    public String getStockFinal() { return String.valueOf(stockFinal); }
}