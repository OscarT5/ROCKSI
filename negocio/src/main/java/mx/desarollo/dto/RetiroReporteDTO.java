package mx.desarollo.dto;

import java.text.NumberFormat;
import java.util.Locale;

public class RetiroReporteDTO {
    private double monto;
    private String observacion;

    public RetiroReporteDTO(double monto, String observacion) {
        this.monto = monto;
        this.observacion = observacion;
    }

    public String getMonto() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "MX")).format(Math.abs(monto));
    }

    public String getObservacion() {
        return observacion;
    }
}