package reportes;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import mx.desarollo.dto.ClienteNuevoDTO;
import mx.desarollo.dto.ProductoMensualDTO;
import mx.desarollo.dto.ReporteMensualDTO;
import mx.desarollo.dto.PagoReporteDTO;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class ReporteMensualPDF {

    public void generarReporte(ReporteMensualDTO datos, OutputStream outputStream) throws IOException {

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        //Logo
        URL logoURL = ReporteDiarioPDF.class.getResource("/reportes/rockon-icon.png");
        if (logoURL != null) {
            ImageData logoData = ImageDataFactory.create(logoURL);
            Image logo = new Image(logoData);
            logo.setWidth(100).setHeight(100);
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            doc.add(logo);
        }

        //Titulo
        Paragraph titulo = new Paragraph("Reporte Mensual " + datos.getMesYAnio())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setMarginBottom(20);
        doc.add(titulo);

        //Secccion de productos
        crearSeccionEncabezado(doc, "Productos");
        Table tablaProductos = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2}));
        tablaProductos.setWidth(UnitValue.createPercentValue(100));
        tablaProductos.addHeaderCell(celdaHeader("Inventario"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad vendida este mes"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad actual"));

        for (ProductoMensualDTO prod : datos.getProductos()) {
            tablaProductos.addCell(celdaCentro(prod.getNombre()));
            tablaProductos.addCell(celdaCentro(prod.getCantidadVendida()));
            tablaProductos.addCell(celdaCentro(prod.getCantidadActual()));
        }
        doc.add(tablaProductos);
        doc.add(new Paragraph("\n"));


        //Seccion de pagos
        crearSeccionEncabezado(doc, "Pagos del Mes");
        Table tablaPagos = new Table(UnitValue.createPercentArray(new float[]{2, 4, 3, 2}));
        tablaPagos.setWidth(UnitValue.createPercentValue(100));
        tablaPagos.addHeaderCell(celdaHeader("ID PAGO"));
        tablaPagos.addHeaderCell(celdaHeader("NOMBRE CLIENTE"));
        tablaPagos.addHeaderCell(celdaHeader("ARTICULO(S)"));
        tablaPagos.addHeaderCell(celdaHeader("TOTAL"));

        if (datos.getPagos() != null && !datos.getPagos().isEmpty()) {
            for (PagoReporteDTO pago : datos.getPagos()) {
                tablaPagos.addCell(celdaCentro(pago.getIdPago()));
                tablaPagos.addCell(celdaCentro(pago.getNombreCliente()));
                tablaPagos.addCell(celdaCentro(pago.getArticulo()));
                tablaPagos.addCell(celdaCentro(pago.getTotal()));
            }
        } else {
            tablaPagos.addCell(new Cell(1, 4).add(new Paragraph("No se registraron pagos este mes."))
                    .setTextAlignment(TextAlignment.CENTER).setPadding(10));
        }
        doc.add(tablaPagos);
        doc.add(new Paragraph("\n"));

        //Seccion de clientes nuevos
        crearSeccionEncabezado(doc, "Clientes Nuevos");
        Table tablaClientes = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2}));
        tablaClientes.setWidth(UnitValue.createPercentValue(100));
        tablaClientes.addHeaderCell(celdaHeader("ID Cliente"));
        tablaClientes.addHeaderCell(celdaHeader("Nombre Cliente"));
        tablaClientes.addHeaderCell(celdaHeader("Membresía Adquirida"));

        if (datos.getClientesNuevos() != null && !datos.getClientesNuevos().isEmpty()) {
            for (ClienteNuevoDTO cliente : datos.getClientesNuevos()) {
                tablaClientes.addCell(celdaCentro(cliente.getIdCliente()));
                tablaClientes.addCell(celdaCentro(cliente.getNombreCompleto()));
                tablaClientes.addCell(celdaCentro(cliente.getMembresia()));
            }
        } else {
            tablaClientes.addCell(new Cell(1, 3).add(new Paragraph("No se registraron clientes nuevos este mes."))
                    .setTextAlignment(TextAlignment.CENTER).setPadding(10));
        }
        doc.add(tablaClientes);
        doc.close();
    }




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