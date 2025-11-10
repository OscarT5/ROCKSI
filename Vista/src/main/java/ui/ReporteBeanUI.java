package ui;

import helper.ReporteHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
// Importa el DTO desde el módulo 'negocio'
import mx.desarollo.dto.ReporteDiarioDTO;
import reportes.ReporteDiarioPDF;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named("reporteBeanUI")
@RequestScoped // Usamos RequestScoped porque la generación es una sola acción
public class ReporteBeanUI implements Serializable {

    private final ReporteHelper reporteHelper = new ReporteHelper();

    public void descargarReporteDiario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        LocalDate hoy = LocalDate.now();
        String nombreArchivo = "Reporte_Diario_" + hoy.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";

        ec.setResponseContentType("application/pdf");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");

        try (OutputStream outputStream = ec.getResponseOutputStream()) {

            ReporteDiarioDTO datos = reporteHelper.obtenerDatosReporte(hoy);

            ReporteDiarioPDF generadorPDF = new ReporteDiarioPDF();

            generadorPDF.generarReporte(datos, outputStream);

            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al generar reporte", "No se pudo crear el PDF: " + e.getMessage()));
        }
    }
}