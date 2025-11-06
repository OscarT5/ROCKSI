package reportes;

public class TestReporteDiario {

    public static void main(String[] args) {
        try {
            String ruta = System.getProperty("user.home") + "/Desktop/Reporte_Diario.pdf";

            ReporteDiarioPDF.generarReporte(ruta);

            System.out.println("Reporte generado: " + ruta);
        } catch (Exception e) {
            System.err.println("Error al generar el reporte:");
            e.printStackTrace();
        }
    }
}
