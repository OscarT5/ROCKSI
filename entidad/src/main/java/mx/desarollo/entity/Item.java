package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Size(max = 45)
    @Column(name = "ID_Item", nullable = false, length = 45)
    private String idItem;

    @NotNull
    @Lob
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

}