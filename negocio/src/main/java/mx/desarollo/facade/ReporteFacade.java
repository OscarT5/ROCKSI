package mx.desarollo.facade;

import mx.desarollo.delegate.ReporteDelegate;
// Importa la nueva ubicación de tu DTO
import mx.desarollo.dto.ReporteDiarioDTO;

import java.time.LocalDate;

public class ReporteFacade {

    private final ReporteDelegate reporteDelegate;

    public ReporteFacade() {
        this.reporteDelegate = new ReporteDelegate();
    }

    public ReporteDiarioDTO obtenerDatosReporteDiario(LocalDate fecha) {
        try {
            return reporteDelegate.generarDatosReporteDiario(fecha);
        } catch (Exception e) {
            throw new RuntimeException("Error en la capa de Facade al generar reporte: " + e.getMessage(), e);
        }
    }
}