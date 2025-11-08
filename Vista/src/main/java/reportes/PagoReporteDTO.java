package reportes;

import java.text.NumberFormat;
import java.util.Locale;

//dto para pasar datos de pagos al PDF
public class PagoReporteDTO {
    String idCliente;
    String nombreCliente;
    String idPago;
    String articulo;
    double total;

    public PagoReporteDTO(String idCliente, String nombreCliente, String idPago, String articulo, double total) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.idPago = idPago;
        this.articulo = articulo;
        this.total = total;
    }

    public String getIdCliente() { return idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getIdPago() { return idPago; }
    public String getArticulo() { return articulo; }

    public String getTotal() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(total);
    }
}