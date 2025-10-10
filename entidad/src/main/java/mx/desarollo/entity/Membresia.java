package mx.desarollo.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "membresia")
public class Membresia implements Serializable {

    @Id
    @Column(name = "ID_Membresia", length = 45)
    private String idMembresia;

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaVencimiento")
    private Date fechaVencimiento;


    @OneToMany(mappedBy = "membresia", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cliente> clientes = new ArrayList<>();


    public Membresia() { }

    public Membresia(String idMembresia, Date fechaVencimiento) {
        this.idMembresia = idMembresia;
        this.fechaVencimiento = fechaVencimiento;
    }


    public void addCliente(Cliente c) {
        if (c != null) {
            clientes.add(c);
            c.setMembresia(this);
        }
    }

    public void removeCliente(Cliente c) {
        if (c != null) {
            clientes.remove(c);
            c.setMembresia(null);
        }
    }

    // Getters y setters
    public String getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(String idMembresia) {
        this.idMembresia = idMembresia;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
