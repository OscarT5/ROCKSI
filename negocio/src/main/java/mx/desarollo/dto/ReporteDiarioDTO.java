package mx.desarollo.dto;

import mx.desarollo.dto.PagoReporteDTO;
import mx.desarollo.dto.ProductoReporteDTO;

import java.time.LocalDate;
import java.util.List;

//Dto principal que agrupa toda la informacion que va a usar el reporte diario
public class ReporteDiarioDTO {

    private LocalDate fecha;
    private List<ProductoReporteDTO> productos;
    private List<PagoReporteDTO> pagos;

    private List<RetiroReporteDTO> retiros;
    private CajaReporteDTO caja;

    public ReporteDiarioDTO(LocalDate fecha, List<ProductoReporteDTO> productos, List<PagoReporteDTO> pagos,List<RetiroReporteDTO> retiros,
                            CajaReporteDTO caja) {
        this.fecha = fecha;
        this.productos = productos;
        this.pagos = pagos;
        this.retiros = retiros;
        this.caja = caja;
    }

    public LocalDate getFecha() { return fecha; }
    public List<ProductoReporteDTO> getProductos() { return productos; }
    public List<PagoReporteDTO> getPagos() { return pagos; }
    public List<RetiroReporteDTO> getRetiros() { return retiros; }
    public CajaReporteDTO getCaja() { return caja; }
}