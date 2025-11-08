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

    /**
     * Este es el método que llamarás desde tu botón en el .xhtml
     * (ej. action="#{reporteBeanUI.descargarReporteDiario}")
     */
    public void descargarReporteDiario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        LocalDate hoy = LocalDate.now();
        String nombreArchivo = "Reporte_Diario_" + hoy.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";

        // Configurar la respuesta HTTP para descarga de PDF
        ec.setResponseContentType("application/pdf");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");

        try (OutputStream outputStream = ec.getResponseOutputStream()) {

            // 1. Obtener los datos (Helper -> Facade -> Delegate -> DAOs)
            ReporteDiarioDTO datos = reporteHelper.obtenerDatosReporte(hoy);

            // 2. Instanciar el generador de PDF
            ReporteDiarioPDF generadorPDF = new ReporteDiarioPDF();

            // 3. Generar el PDF y escribirlo en el OutputStream
            generadorPDF.generarReporte(datos, outputStream);

            // 4. Finalizar la respuesta JSF
            fc.responseComplete();

        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al generar reporte", "No se pudo crear el PDF: " + e.getMessage()));
        }
    }
}