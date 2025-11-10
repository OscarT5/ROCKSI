package helper;

import mx.desarollo.dto.ReporteMensualDTO;
import mx.desarollo.integration.ServiceFacadeLocator;
import java.io.Serializable;
import java.time.LocalDate;

public class ReporteMensualHelper implements Serializable {

    public ReporteMensualDTO obtenerDatosReporteMensual(LocalDate fecha) throws Exception {
        try {
            return ServiceFacadeLocator.getInstanceReporteMensualFacade().obtenerDatosReporteMensual(fecha);
        } catch (Exception e) {
            throw new Exception("Error al obtener datos para el reporte mensual: " + e.getMessage());
        }
    }
}