package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "producto")
@PrimaryKeyJoinColumn(name = "ID_Producto")
public class Producto extends Item {

    private static int contador = 1000;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    public Producto() {
        super();
    }

    public Producto(String nombre, int stock) {
        super(generarNuevoId(),null);
        this.nombre = nombre;
        this.stock = stock;
    }

    public static synchronized String generarNuevoId() {
        return "PR" + (contador++);
    }

    public static void setContador(int valor) {
        contador = valor;
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
