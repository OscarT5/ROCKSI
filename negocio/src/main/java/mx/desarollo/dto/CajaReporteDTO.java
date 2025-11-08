package mx.desarollo.dto;

import java.text.NumberFormat;
import java.util.Locale;

public class CajaReporteDTO {
    private double dineroInicial;
    private double dineroDeberiaTerminar;
    private double dineroTermino;

    public CajaReporteDTO(double dineroInicial, double dineroDeberiaTerminar, double dineroTermino) {
        this.dineroInicial = dineroInicial;
        this.dineroDeberiaTerminar = dineroDeberiaTerminar;
        this.dineroTermino = dineroTermino;
    }
    public String getDineroInicial() {
        return formatCurrency(dineroInicial);
    }
    public String getDineroDeberiaTerminar() {
        return formatCurrency(dineroDeberiaTerminar);
    }
    public String getDineroTermino() {
        return formatCurrency(dineroTermino);
    }

    private String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(amount);
    }
}