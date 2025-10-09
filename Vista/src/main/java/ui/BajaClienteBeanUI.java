package ui;

import helper.ClienteHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("bajaClienteBeanUI") //Se le asigna el nombre que se usara en el xhtml
@SessionScoped//Se indica que la sesion terminara una vez cerrado el formulario
public class BajaClienteBeanUI implements Serializable {

    private String idCliente;
    private final ClienteHelper clienteHelper = new ClienteHelper();

    //Este metodo utiliza el id de un cliente para pasarselo a las otras capas y eliminarlo
    public void eliminarCliente() {
        try {
            clienteHelper.eliminarCliente(idCliente);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Baja Exitosa", "Cliente eliminado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar",e.getMessage()));
        }
    }

    public String getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
}
