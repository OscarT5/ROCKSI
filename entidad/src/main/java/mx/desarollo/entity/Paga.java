package mx.desarollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "paga")
public class Paga {

    //Contador utilizado para la generacion de ids
    private static int contador = 1000;

    //Esto se utiliza para obtener el ID creado en el dao (solo el numero)
    public static void setContador(int valor) {
        contador = valor;
    }

    //Aqui se genera el id
    public static synchronized String generarNuevoId() {
        return "GPA" + (contador++);
    }

    @Id
    @Size(max = 45)
    @Column(name = "ID_Paga", nullable = false, length = 45)
    private String idPaga;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_Cliente", nullable = false)
    private Cliente idCliente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_Item", nullable = false)
    private Item idItem;

    @Size(max = 45)
    @NotNull
    @Column(name = "ID_UsuarioRecep", nullable = false, length = 45)
    private String idUsuariorecep;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Double monto;

    @NotNull
    @Column(name = "porPagar", nullable = false)
    private Byte porPagar;

    public Paga() {
    }

    public String getIdPaga() {
        return idPaga;
    }

    public void setIdPaga(String idPaga) {
        this.idPaga = idPaga;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Item getIdItem() {
        return idItem;
    }

    public void setIdItem(Item idItem) {
        this.idItem = idItem;
    }

    public String getIdUsuariorecep() {
        return idUsuariorecep;
    }

    public void setIdUsuariorecep(String idUsuariorecep) {
        this.idUsuariorecep = idUsuariorecep;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Byte getPorPagar() {
        return porPagar;
    }

    public void setPorPagar(Byte porPagar) {
        this.porPagar = porPagar;
    }

}