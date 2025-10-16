package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "producto")
@PrimaryKeyJoinColumn(name = "ID_Producto") //Une el id de producto con el de item
public class Producto extends Item implements Serializable {

    @NotNull
    @Size(max = 45)
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    private static int contador = 1000;

    public static void setContador(int nuevoContador) {
        contador = nuevoContador;
    }

    public static String generarNuevoId() {
        return "PR" + contador++;
    }

    public Producto() {
        super();
    }

    public Producto(String id, double precio, String nombre, Integer stock) {
        super(id, precio);
        this.nombre = nombre;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
