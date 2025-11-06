package ui;

import helper.ClienteHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;
import org.primefaces.PrimeFaces;

import java.io.Serializable;

@Named("adelantarPagoBeanUI")
@SessionScoped
public class AdelantarPagoBeanUI implements Serializable {

    private String idCliente;
    private Double montoAdelantado;
    private Cliente clienteEncontrado;

    private final ClienteHelper clienteHelper = new ClienteHelper();

    // metodo para buscar cliente por id
    public void buscarCliente() {
        try {
            clienteEncontrado = clienteHelper.obtenerCliente(idCliente);

            if (clienteEncontrado != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Cliente encontrado", "Cliente: " + clienteEncontrado.getNombreCompleto()));

                PrimeFaces.current().executeScript("PF('dlgBuscarClienteAdelanto').hide(); PF('dlgMontoAdelanto').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "No encontrado", "No existe un cliente con el ID ingresado."));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo buscar el cliente."));
            e.printStackTrace();
        }
    }

    // metodo para sumar credito al cliente
    public void registrarAdelanto() {
        try {
            if (clienteEncontrado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Debe seleccionar un cliente valido."));
                return;
            }

            if (montoAdelantado == null || montoAdelantado <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Debe ingresar un monto mayor a 0."));
                return;
            }

            double nuevoCredito = clienteEncontrado.getCredito() + montoAdelantado;
            clienteEncontrado.setCredito(nuevoCredito);
            clienteHelper.ModificarCliente(clienteEncontrado);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Exito", "Credito actualizado. Nuevo saldo: $" + nuevoCredito));

            PrimeFaces.current().executeScript("PF('dlgMontoAdelanto').hide(); PF('dlgConfirmacionAdelanto').show();");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo actualizar el credito."));
            e.printStackTrace();
        }
    }

    // getters y setters
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Double getMontoAdelantado() {
        return montoAdelantado;
    }

    public void setMontoAdelantado(Double montoAdelantado) {
        this.montoAdelantado = montoAdelantado;
    }

    public Cliente getClienteEncontrado() {
        return clienteEncontrado;
    }

    public void setClienteEncontrado(Cliente clienteEncontrado) {
        this.clienteEncontrado = clienteEncontrado;
    }
}
