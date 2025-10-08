package mx.desarollo.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity // indica que esta clase es una entidad jpa
@Table(name = "cliente") // mapea la entidad a la tabla 'cliente' en la bd
public class Cliente { // inicio de la clase cliente

    @Id // marca el campo como clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // el valor se genera automaticamente en la bd
    @Column(name = "ID_Cliente") // nombre de la columna en la bd
    private int idCliente; // id unico del cliente

    @Column(name = "nombreCompleto", nullable = false, length = 100) // columna para el nombre completo
    private String nombreCompleto; // nombre completo del cliente

    @Column(name = "telefono", nullable = false, unique = true, length = 15) // columna para telefono, unica
    private String telefono; // telefono del cliente

    @Temporal(TemporalType.DATE) // guarda solo la fecha (sin hora)
    @Column(name = "fechaRegistro") // columna para la fecha de registro
    private Date fechaRegistro; // fecha en que se registro el cliente

    @ManyToOne(fetch = FetchType.LAZY) // relacion muchos a uno con membresia, carga perezosa
    @JoinColumn(name = "ID_Membresia") // columna foranea que referencia la membresia
    private Membresia idMembresia; // referencia a la membresia del cliente

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true) // lista de pagos vinculados al cliente
    private List<Pago> historialCompras; // historial de compras o pagos del cliente

    @ManyToMany(mappedBy = "cliente", cascade = CascadeType.ALL) // relacion many to many con clases
    private List<Clase> clases; // lista de clases en las que participa el cliente

    @Column(name = "credito") // columna para el credito disponible
    private double credito; // credito disponible para el cliente

    //constructores
    public Cliente() {} // constructor vacio requerido por jpa

    public Cliente(String nombreCompleto, String telefono, Date fechaRegistro,
                   Membresia idMembresia, double credito) { // constructor con campos principales
        this.nombreCompleto = nombreCompleto; // asigna nombre
        this.telefono = telefono; // asigna telefono
        this.fechaRegistro = fechaRegistro; // asigna fecha de registro
        this.idMembresia = idMembresia; // asigna membresia
        this.credito = credito; // asigna credito
    }

    //getters y setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Membresia getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(Membresia idMembresia) {
        this.idMembresia = idMembresia;
    }

    public List<Compra> getHistorialCompras() {
        return historialCompras;
    }

    public void setHistorialCompras(List<Compra> historialCompras) {
        this.historialCompras = historialCompras;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }
} //hola