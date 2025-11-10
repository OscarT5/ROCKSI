package mx.desarollo.facade;

import mx.desarollo.delegate.ReporteMensualDelegate;
import mx.desarollo.dto.ReporteMensualDTO; // Importa el DTO
import java.time.LocalDate;

public class ReporteMensualFacade {

    private final ReporteMensualDelegate reporteDelegate;

    public ReporteMensualFacade() {
        this.reporteDelegate = new ReporteMensualDelegate();
    }

    public ReporteMensualDTO obtenerDatosReporteMensual(LocalDate fecha) {
        try {
            return reporteDelegate.generarDatosReporteMensual(fecha);
        } catch (Exception e) {
            throw new RuntimeException("Error en Facade al generar reporte mensual: " + e.getMessage(), e);
        }
    }
}