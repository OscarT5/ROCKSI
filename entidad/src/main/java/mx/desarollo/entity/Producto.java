package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.io.Serializable;

@Entity
@Table(name = "producto")
@PrimaryKeyJoinColumn(name = "ID_Producto") //Aqui se vincula el id con el id de item
public class Producto extends Item implements Serializable {

    //Contador utilizado para la generacion de ids
    private static int contador = 1000;

    //Esto se utiliza para obtener el ID creado en el dao (solo el numero)
    public static void setContador(int valor) {
        contador = valor;
    }

    //Aqui se genera el id
    public static synchronized String generarNuevoId() {
        return "PR" + (contador++);
    }

    public Producto(String idProducto, String nombre, int stock) {
        super(idProducto, null);
        this.nombre = nombre;
        this.stock = stock;
    }

    @Id
    @Size(max = 45)
    @Column(name = "ID_Producto", nullable = false, length = 45)
    private String idProducto;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ID_Producto", nullable = false)
    private Item item;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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