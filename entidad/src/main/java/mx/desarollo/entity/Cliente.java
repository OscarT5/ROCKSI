package mx.desarollo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @Column(name = "ID_Cliente", length = 45)
    private String idCliente;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombreCompleto", nullable = false, length = 100)
    private String nombreCompleto;

    @Size(max = 15)
    @NotNull
    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @NotNull
    @Column(name = "credito", nullable = false)
    private Long credito;
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaRegistro")
    private Date fechaRegistro;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Membresia")
    private Membresia membresia;
    */

     /*
     @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pago> historialPagos = new ArrayList<>();

    @ManyToMany
    @JoinTable( //Aqui se hace un join para realizar la tabla puente de estainscrito dentro de la BD
            name = "estainscrito",
            joinColumns = @JoinColumn(name = "ID_Cliente"),
            inverseJoinColumns = @JoinColumn(name = "ID_Clase")
    )
    private List<Clase> clases = new ArrayList<>();
      */



    @NotNull
    @Column(name = "fechaRegistro", nullable = false)
    private LocalDate fechaRegistro;

    @Size(max = 45)
    @NotNull
    @Column(name = "ID_Membresia", nullable = false, length = 45)
    private String idMembresia;
    //constructores
    public Cliente() {
        this.idCliente = UUID.randomUUID().toString();//Esto asigna un id aleatorio, MODIFICAR DESPUES
        this.fechaRegistro = new Date();//Asigna el dia de hoy
    }

    @Column(name = "credito")
    private double credito;

    //constructores
    public Cliente() {}

    public Cliente(String nombreCompleto, String telefono, Date fechaRegistro,
                   Membresia idMembresia, double credito) {
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        //this.membresia = membresia;
        this.credito = credito;
    }

    //getters y setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getCredito() {
        return credito;
    }

    public void setCredito(Long credito) {
        this.credito = credito;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(String idMembresia) {
        this.idMembresia = idMembresia;
    }

}