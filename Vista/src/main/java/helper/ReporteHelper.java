package helper;

import mx.desarollo.dto.ReporteDiarioDTO;
import mx.desarollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.time.LocalDate;

public class ReporteHelper implements Serializable {

    public ReporteDiarioDTO obtenerDatosReporte(LocalDate fecha) throws Exception {
        try {
            return ServiceFacadeLocator.getInstanceReporteFacade().obtenerDatosReporteDiario(fecha);
        } catch (Exception e) {
            throw new Exception("Error al obtener datos para el reporte: " + e.getMessage());
        }
    }
}