package reportes;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import mx.desarollo.dto.PagoReporteDTO;
import mx.desarollo.dto.ProductoReporteDTO;
import mx.desarollo.dto.ReporteDiarioDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;

/**
 * Clase refactorizada para generar el PDF del Reporte Diario.
 * Ahora acepta un DTO con los datos y escribe a un OutputStream.
 */
public class ReporteDiarioPDF {

    // Ya no es un método estático
    public void generarReporte(ReporteDiarioDTO datos, OutputStream outputStream) throws IOException {

        // El PdfWriter ahora escribe al 'outputStream' (la descarga del navegador)
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // --- 1. LOGO ---
        URL logoURL = ReporteDiarioPDF.class.getResource("/reportes/rockon-icon.png");
        if (logoURL == null) {
            // No lanzamos excepción, solo imprimimos en consola para no detener el reporte
            System.err.println("No se encontro el archivo de logo en /reportes/rockon-icon.png");
        } else {
            ImageData logoData = ImageDataFactory.create(logoURL);
            Image logo = new Image(logoData);
            logo.setWidth(100).setHeight(100);
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(logo);
        }

        // --- 2. TÍTULO (DINÁMICO) ---
        String fechaFormateada = datos.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Paragraph titulo = new Paragraph("Reporte Diario " + fechaFormateada)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setMarginBottom(20);
        doc.add(titulo);

        // --- 3. SECCIÓN DE PRODUCTOS (DINÁMICO) ---
        crearSeccionEncabezado(doc, "Productos");

        Table tablaProductos = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2}));
        tablaProductos.setWidth(UnitValue.createPercentValue(100));
        tablaProductos.addHeaderCell(celdaHeader("Inventario"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad inicial"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad vendida"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad final"));

        // Bucle dinámico usando los datos del DTO
        for (ProductoReporteDTO prod : datos.getProductos()) {
            tablaProductos.addCell(celdaCentro(prod.getNombre()));
            tablaProductos.addCell(celdaCentro(prod.getStockInicial()));
            tablaProductos.addCell(celdaCentro(prod.getCantidadVendida()));
            tablaProductos.addCell(celdaCentro(prod.getStockFinal()));
        }
        doc.add(tablaProductos);
        doc.add(new Paragraph("\n"));

        // --- 4. RETIROS DE CAJA (PENDIENTE) ---
        // TODO: Implementar la lógica para Retiros de Caja
        crearSeccionEncabezado(doc, "Retiros de caja");

        Table tablaRetiros = new Table(UnitValue.createPercentArray(new float[]{2, 3}));
        tablaRetiros.setWidth(UnitValue.createPercentValue(100));
        tablaRetiros.addHeaderCell(celdaHeader("Cantidad Retirada"));
        tablaRetiros.addHeaderCell(celdaHeader("Observaciones"));

        // Datos de ejemplo por ahora
        tablaRetiros.addCell(celdaCentro("$1000.21"));
        tablaRetiros.addCell(celdaCentro("Retiro para pago de empleado1"));

        doc.add(tablaRetiros);
        doc.add(new Paragraph("\n"));

        // --- 5. PAGOS (DINÁMICO) ---
        crearSeccionEncabezado(doc, "Pagos");

        Table tablaPagos = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 3, 2}));
        tablaPagos.setWidth(UnitValue.createPercentValue(100));
        tablaPagos.addHeaderCell(celdaHeader("ID CLIENTE"));
        tablaPagos.addHeaderCell(celdaHeader("NOMBRE CLIENTE"));
        tablaPagos.addHeaderCell(celdaHeader("ID PAGO"));
        tablaPagos.addHeaderCell(celdaHeader("ARTICULO(S)"));
        tablaPagos.addHeaderCell(celdaHeader("TOTAL"));

        // Bucle dinámico usando los datos del DTO
        for (PagoReporteDTO pago : datos.getPagos()) {
            tablaPagos.addCell(celdaCentro(pago.getIdCliente()));
            tablaPagos.addCell(celdaCentro(pago.getNombreCliente()));
            tablaPagos.addCell(celdaCentro(pago.getIdPago()));
            tablaPagos.addCell(celdaCentro(pago.getArticulo()));
            tablaPagos.addCell(celdaCentro(pago.getTotal())); // El DTO ya formatea el dinero
        }
        doc.add(tablaPagos);
        doc.add(new Paragraph("\n"));

        // --- 6. SECCIÓN DE CAJA (PENDIENTE) ---
        // TODO: Implementar la lógica para Cierre de Caja
        crearSeccionEncabezado(doc, "Caja");

        doc.add(new Paragraph("Dinero con el que inició caja").setBold());
        doc.add(new Paragraph("$200\n"));
        doc.add(new Paragraph("Dinero con el que debería terminar caja").setBold());
        doc.add(new Paragraph("$1000.21\n"));
        doc.add(new Paragraph("Dinero con el que terminó caja").setBold());
        doc.add(new Paragraph("$1000.21"));

        // Cerramos el documento
        doc.close();
    }


    // --- MÉTODOS DE UTILIDAD (Helpers de iText) ---

    private static void crearSeccionEncabezado(Document doc, String titulo) {
        Paragraph header = new Paragraph(titulo)
                .setBackgroundColor(ColorConstants.BLUE)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setPadding(5)
                .setFontSize(14)
                .setMarginBottom(10);
        doc.add(header);
    }

    private static Cell celdaHeader(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell celdaCentro(String texto) {
        return new Cell()
                .add(new Paragraph(texto))
                .setTextAlignment(TextAlignment.CENTER);
    }
}