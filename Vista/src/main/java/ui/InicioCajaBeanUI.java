package ui;

import helper.ClienteHelper;
import helper.PagaHelper;
import helper.UsuarioRHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Paga;
import mx.desarollo.entity.Usuariorecepcionista;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDate;

@Named("inicioCajaBeanUI")
@SessionScoped
public class InicioCajaBeanUI implements Serializable {

    private String idUsuarioRecep;
    private Double montoEnCaja;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioValidado;

    private final PagaHelper pagaHelper = new PagaHelper();
    private final ClienteHelper clienteHelper = new ClienteHelper();
    private final UsuarioRHelper usuarioRHelper = new UsuarioRHelper();

    private static final String ID_ITEM_APERTURA = "AC1000";
    private static final String ID_CLIENTE_TIENDA = "CLI1000";

    // Verifica el ID del recepcionista
    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (idUsuarioRecep == null || idUsuarioRecep.trim().isEmpty()) {
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");
            }

            usuarioValidado = usuarioRHelper.obtenerUsuarioR(idUsuarioRecep.trim());

            if (usuarioValidado == null) {
                throw new Exception("No se encontró un usuario con ese ID.");
            }

        } catch (Exception e) {
            usuarioValidado = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar", e.getMessage()));
        }
    }

    // Valida la contraseña del recepcionista
    public void validarContrasena() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioValidado == null) {
                fc.validationFailed();
                throw new Exception("Debe verificar primero al usuario recepcionista.");
            }
            if (contrasenaUR == null || contrasenaUR.trim().isEmpty()) {
                fc.validationFailed();
                throw new Exception("Debe ingresar la contraseña.");
            }
            if (!usuarioValidado.getContrasena().equals(contrasenaUR)) {
                fc.validationFailed();
                throw new Exception("Contraseña incorrecta.");
            }

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", e.getMessage()));
        }
    }

    // Lógica para registrar la apertura de caja (ingreso inicial)
    public void registrarAperturaCaja() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioValidado == null) {
                throw new Exception("No se ha validado un recepcionista.");
            }
            if (montoEnCaja == null || montoEnCaja <= 0) {
                throw new Exception("El monto de apertura debe ser mayor a 0.");
            }

            Cliente clienteTienda = clienteHelper.obtenerCliente(ID_CLIENTE_TIENDA);
            if (clienteTienda == null) {
                throw new Exception("Error crítico: El cliente marcador '" + ID_CLIENTE_TIENDA + "' no existe.");
            }

            Paga apertura = new Paga();
            apertura.setIdCliente(clienteTienda);
            apertura.setIdUsuariorecep(usuarioValidado.getIdUsuariorecep());
            apertura.setFecha(LocalDate.now());
            apertura.setMonto(montoEnCaja);
            apertura.setPorPagar((byte) 0);

            pagaHelper.RealizarPago(apertura, ID_ITEM_APERTURA);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Apertura de caja registrada por $" + montoEnCaja + "."));
            PrimeFaces.current().ajax().update("formPagos:tablaPagos");
            PrimeFaces.current().executeScript("PF('dlgMontoApertura').hide(); PF('dlgConfirmacionApertura').show();");

            limpiar();

        } catch (Exception e) {
            fc.addMessage("msgsMontoApertura",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Registrar", e.getMessage()));
            e.printStackTrace();
        }
    }

    public void limpiar() {
        this.idUsuarioRecep = null;
        this.contrasenaUR = null;
        this.montoEnCaja = null;
        this.usuarioValidado = null;
    }

    // Getters y Setters
    public String getIdUsuarioRecep() { return idUsuarioRecep; }
    public void setIdUsuarioRecep(String idUsuarioRecep) { this.idUsuarioRecep = idUsuarioRecep; }

    public Double getMontoEnCaja() { return montoEnCaja; }
    public void setMontoEnCaja(Double montoEnCaja) { this.montoEnCaja = montoEnCaja; }

    public String getContrasenaUR() { return contrasenaUR; }
    public void setContrasenaUR(String contrasenaUR) { this.contrasenaUR = contrasenaUR; }
}
