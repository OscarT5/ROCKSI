package mx.desarollo.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
//La clase item sera una superclase que puede convertirse en 3, las cuales seran clase, membresia y producto
@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED) // ← estrategia de herencia
public abstract class Item implements Serializable {

    @Id
    @Column(name = "ID_Item", length = 45, nullable = false)
    private String idItem;

    @Column(name = "precio", nullable = true, precision = 10, scale = 2)
    private double precio;

    public Item() {
    }

    public Item(String idItem, double precio) {
        this.idItem = idItem;
        this.precio = precio;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
