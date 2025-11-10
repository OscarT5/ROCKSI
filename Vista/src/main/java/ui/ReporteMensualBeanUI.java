package ui;

import helper.ReporteMensualHelper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.dto.ReporteMensualDTO;
import reportes.ReporteMensualPDF;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Named("reporteMensualBeanUI")
@RequestScoped
public class ReporteMensualBeanUI implements Serializable {

    private final ReporteMensualHelper reporteHelper = new ReporteMensualHelper();

    public void descargarReporteMensual() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        LocalDate hoy = LocalDate.now();
        String nombreArchivo = "Reporte_Mensual_" +
                hoy.format(DateTimeFormatter.ofPattern("MMMM_yyyy", new Locale("es", "ES"))) +
                ".pdf";
        ec.setResponseContentType("application/pdf");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");

        try (OutputStream outputStream = ec.getResponseOutputStream()) {

            //Obtener los datos
            ReporteMensualDTO datos = reporteHelper.obtenerDatosReporteMensual(hoy);
            ReporteMensualPDF generadorPDF = new ReporteMensualPDF();
            //HGebera el pdf
            generadorPDF.generarReporte(datos, outputStream);
            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al generar reporte", "No se pudo crear el PDF: " + e.getMessage()));
        }
    }
}