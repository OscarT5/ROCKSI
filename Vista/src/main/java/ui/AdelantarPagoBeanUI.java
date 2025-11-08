package ui;

import helper.ClienteHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;
import helper.PagaHelper;
import mx.desarollo.entity.Paga;
import mx.desarollo.entity.Usuariorecepcionista;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDate;

@Named("adelantarPagoBeanUI")
@SessionScoped
public class AdelantarPagoBeanUI implements Serializable {

    private String idCliente;
    private Double montoAdelantado;
    private Cliente clienteEncontrado;
    private String idUsuarioRecep;

    private final ClienteHelper clienteHelper = new ClienteHelper();
    private final PagaHelper pagaHelper = new PagaHelper();

    private static final String ID_ITEM_ADELANTO = "PAD1000";

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
            // se valida el ID de recepcionista
            if (idUsuarioRecep == null || idUsuarioRecep.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("msgsMontoAdelanto",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "ID de Recepcionista no encontrado. Vuelva a empezar el proceso."));
                return;
            }
            if (montoAdelantado == null || montoAdelantado <= 0) {
                FacesContext.getCurrentInstance().addMessage("msgsMontoAdelanto",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Debe ingresar un monto mayor a 0."));
                return;
            }

            // se crea el objeto de paga
            Paga pagoAdelanto = new Paga();
            pagoAdelanto.setIdCliente(clienteEncontrado);
            pagoAdelanto.setMonto(montoAdelantado);
            pagoAdelanto.setFecha(LocalDate.now());
            pagoAdelanto.setIdUsuariorecep(idUsuarioRecep);
            pagoAdelanto.setPorPagar((byte) 0);

            pagaHelper.RealizarPago(pagoAdelanto, ID_ITEM_ADELANTO);

            double nuevoCredito = clienteEncontrado.getCredito() + montoAdelantado;
            clienteEncontrado.setCredito(nuevoCredito);
            clienteHelper.ModificarCliente(clienteEncontrado);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Exito", "Crédito actualizado y pago registrado. Nuevo saldo: $" + nuevoCredito));


            PrimeFaces.current().ajax().update("formPagos:tablaPagos");

            PrimeFaces.current().executeScript("PF('dlgMontoAdelanto').hide(); PF('dlgConfirmacionAdelanto').show();");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("msgsMontoAdelanto",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo registrar el adelanto: " + e.getMessage()));
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

    public String getIdUsuarioRecep() {
        return idUsuarioRecep;
    }

    public void setIdUsuarioRecep(String idUsuarioRecep) {
        this.idUsuarioRecep = idUsuarioRecep;
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
