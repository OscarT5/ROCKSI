package reportes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//Dto principal que agrupa toda la informacion que va a usar el reporte diario
public class ReporteDiarioDTO {

    private LocalDate fecha;
    private List<ProductoReporteDTO> productos;
    private List<PagoReporteDTO> pagos;

    public ReporteDiarioDTO(LocalDate fecha, List<ProductoReporteDTO> productos, List<PagoReporteDTO> pagos) {
        this.fecha = fecha;
        this.productos = productos;
        this.pagos = pagos;
    }

    public LocalDate getFecha() { return fecha; }
    public List<ProductoReporteDTO> getProductos() { return productos; }
    public List<PagoReporteDTO> getPagos() { return pagos; }
}