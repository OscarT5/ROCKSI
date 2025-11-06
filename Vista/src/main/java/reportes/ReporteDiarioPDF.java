package reportes;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.IOException;
import java.net.URL;

public class ReporteDiarioPDF {

    public static void generarReporte(String destino) throws IOException {
        PdfWriter writer = new PdfWriter(destino);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        //logo
        URL logoURL = ReporteDiarioPDF.class.getResource("/reportes/rockon-icon.png");
        if (logoURL == null) {
            throw new IOException("No se encontro el archivo de logo en /reportes/rockon-icon.png");
        }
        ImageData logoData = ImageDataFactory.create(logoURL);
        Image logo = new Image(logoData);
        logo.setWidth(100).setHeight(100);
        logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(logo);

        //titulo
        Paragraph titulo = new Paragraph("Reporte Diario DD/MM/YYYY")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setMarginBottom(20);
        doc.add(titulo);

        //Seccion de procuctos
        crearSeccionEncabezado(doc, "Productos");

        Table tablaProductos = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2}));
        tablaProductos.setWidth(UnitValue.createPercentValue(100));
        tablaProductos.addHeaderCell(celdaHeader("Inventario"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad inicial"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad vendida"));
        tablaProductos.addHeaderCell(celdaHeader("Cantidad final"));

        String[][] productos = {
                {"Doritos", "20", "20", "20"},
                {"Sabritas", "20", "20", "20"},
                {"Coca-Cola", "20", "20", "20"},
                {"Pepsi", "20", "20", "20"},
                {"Takis", "20", "20", "20"},
                {"Palomitas", "20", "20", "20"}
        };

        for (String[] fila : productos) {
            for (String dato : fila) {
                tablaProductos.addCell(new Cell().add(new Paragraph(dato)).setTextAlignment(TextAlignment.CENTER));
            }
        }
        doc.add(tablaProductos);
        doc.add(new Paragraph("\n"));

        //retiros de caja
        crearSeccionEncabezado(doc, "Retiros de caja");

        Table tablaRetiros = new Table(UnitValue.createPercentArray(new float[]{2, 3}));
        tablaRetiros.setWidth(UnitValue.createPercentValue(100));
        tablaRetiros.addHeaderCell(celdaHeader("Cantidad Retirada"));
        tablaRetiros.addHeaderCell(celdaHeader("Observaciones"));

        String[][] retiros = {
                {"$1000.21", "Retiro para pago de empleado1"},
                {"$1000.21", "Retiro para pago de empleado2"},
                {"$1000.21", "Retiro para pago de empleado1"},
                {"$1000.21", "Retiro para pago de empleado2"},
        };
        for (String[] fila : retiros) {
            for (String dato : fila) {
                tablaRetiros.addCell(new Cell().add(new Paragraph(dato)).setTextAlignment(TextAlignment.CENTER));
            }
        }
        doc.add(tablaRetiros);
        doc.add(new Paragraph("\n"));

        //pagos
        crearSeccionEncabezado(doc, "Pagos");

        Table tablaPagos = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 3, 2}));
        tablaPagos.setWidth(UnitValue.createPercentValue(100));
        tablaPagos.addHeaderCell(celdaHeader("ID CLIENTE"));
        tablaPagos.addHeaderCell(celdaHeader("NOMBRE CLIENTE"));
        tablaPagos.addHeaderCell(celdaHeader("ID PAGO"));
        tablaPagos.addHeaderCell(celdaHeader("ARTICULO(S)"));
        tablaPagos.addHeaderCell(celdaHeader("TOTAL"));

        for (int i = 0; i < 8; i++) {
            tablaPagos.addCell("CLI10001");
            tablaPagos.addCell("Alexandro Fregoso Castro");
            tablaPagos.addCell("PA10001");
            tablaPagos.addCell("Membresía");
            tablaPagos.addCell("$1000.21");
        }
        doc.add(tablaPagos);
        doc.add(new Paragraph("\n"));

        //seccion de caja
        crearSeccionEncabezado(doc, "Caja");

        doc.add(new Paragraph("Dinero con el que inició caja").setBold());
        doc.add(new Paragraph("$200\n"));
        doc.add(new Paragraph("Dinero con el que debería terminar caja").setBold());
        doc.add(new Paragraph("$1000.21\n"));
        doc.add(new Paragraph("Dinero con el que terminó caja").setBold());
        doc.add(new Paragraph("$1000.21"));

        doc.close();
        System.out.println("Reporte generado en: " + destino);
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
}
