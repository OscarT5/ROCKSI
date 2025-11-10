package mx.desarollo.dto;

import java.util.List;

//dto que agrupa toda la informacion del reporte mensual
public class ReporteMensualDTO {

    private String mesYAnio;
    private List<ProductoMensualDTO> productos;
    private List<ClienteNuevoDTO> clientesNuevos;
    private List<PagoReporteDTO> pagos;

    public ReporteMensualDTO(String mesYAnio, List<ProductoMensualDTO> productos, List<ClienteNuevoDTO> clientesNuevos, List<PagoReporteDTO> pagos) {
        this.mesYAnio = mesYAnio;
        this.productos = productos;
        this.clientesNuevos = clientesNuevos;
        this.pagos = pagos;
    }

    public String getMesYAnio() { return mesYAnio; }
    public List<ProductoMensualDTO> getProductos() { return productos; }
    public List<ClienteNuevoDTO> getClientesNuevos() { return clientesNuevos; }
    public List<PagoReporteDTO> getPagos() { return pagos; }
}