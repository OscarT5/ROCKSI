package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "membresia")
@PrimaryKeyJoinColumn(name = "ID_Membresia", referencedColumnName = "ID_Item")
public class Membresia extends Item implements Serializable {

    //Contador utilizado para la generacion de ids
    private static int contador = 1000;

    //Esto se utiliza para obtener el ID creado en el dao (solo el numero)
    public static void setContador(int valor) {
        contador = valor;
    }

    //Aqui se genera el id
    public static synchronized String generarNuevoId() {
        return "M" + (contador++);
    }

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ID_Membresia", nullable = false)
    private Item item;

    @NotNull
    @Column(name = "fechaVencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ID_Cliente", nullable = false)
    private Cliente idCliente;

    public Membresia() {
        super();
    }

    public Membresia(String id, Cliente idCliente, LocalDate fechaVencimiento ) {
        super(id);
        this.idCliente =  idCliente;
        this.fechaVencimiento = fechaVencimiento;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

}