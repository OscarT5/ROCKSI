package ui;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("AsignarClaseBeanUI")
@SessionScoped
public class AsignarClaseBeanUI implements Serializable {
    private String idCliente;
    private String idClase;

    /*
    FALTA IMPLEMENTAR
     */

    // Getters y setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }
}
