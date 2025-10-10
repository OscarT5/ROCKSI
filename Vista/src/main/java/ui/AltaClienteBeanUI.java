package ui;
import helper.ClienteHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;

import java.io.Serializable;

//Nombre del Bean
@Named("altaCliBeanUI")
@SessionScoped
//Clase principal
public class AltaClienteBeanUI implements Serializable {
    private Cliente cliente = new Cliente(); //Crea un objeto de tipo cliente
    private ClienteHelper guardarCliente = new ClienteHelper(); //Crea un objeto de tipo ClienteHelper
    private String nombre; //String que se llenara de acuerdo a lo que la vista obtenga
    private String apellido; //String que se llenara de acuerdo a lo que la vista obtenga
    private String telefono; //String que se llenara de acuerdo a lo que la vista obtenga

    /**
     * Realiza un alta de cliente
     * Obtiene los datos ingresados de la vista y los guarda en las variables correspondientes
     * dichas variables se utilizan para darle identidad al objeto cliente
     * con el objeto de tipo Helper se manda llamara la funcion Alta cliente que recibe al cliente con identidad
     * @Throws Si la alta no se realiza con exito
     * @return Alta exitosa, si se registra el cliente correctamente en la base de datos
     * @return Alta invalida, si la base de datos rechaza el registro
     */
    public void altaCliente() {
        try {
            this.cliente.setNombreCompleto(this.nombre + " " + this.apellido);
            this.cliente.setTelefono(this.telefono);
            this.guardarCliente.AltaCliente(this.cliente);
            FacesContext.getCurrentInstance().addMessage((String)null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alta Exitosa", "Cliente creado..."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage((String)null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alta Invalida", e.getMessage()));
        }

    }

    //Getters y Setters
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
