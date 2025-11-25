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

@Named("retirarDineroPagaBeanUI")
@SessionScoped
public class RetirarDineroPagaBeanUI implements Serializable {

    private String idUsuarioRecep;
    private Double montoRetirar;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioValidado;
    private String observaciones;

    private final PagaHelper pagaHelper = new PagaHelper();
    private final ClienteHelper clienteHelper = new ClienteHelper();
    private final UsuarioRHelper usuarioRHelper = new UsuarioRHelper();

    private static final String ID_ITEM_RETIRO = "RC1000";
    private static final String ID_CLIENTE_TIENDA = "CLI69";

    // se verifica el id del recepcionista
    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (idUsuarioRecep == null || idUsuarioRecep.trim().isEmpty()) {
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");
            }

            // se busca el usuario en la bd
            usuarioValidado = usuarioRHelper.obtenerUsuarioR(idUsuarioRecep.trim());

            // se valida si existe
            if (usuarioValidado == null) {
                throw new Exception("No se encontró un usuario con ese ID.");
            }

            // nueva validacion del estatus
            if (usuarioValidado.getEstatus() == 0) {
                usuarioValidado = null;
                throw new Exception("El usuario está dado de BAJA y no tiene permisos para realizar retiros.");
            }

        } catch (Exception e) {
            usuarioValidado = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar", e.getMessage()));
        }
    }

    // se valida la contraseña
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

    // logica para registrar el retiro de dinero de caja
    public void registrarRetiro() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioValidado == null) {
                throw new Exception("No se ha validado un recepcionista.");
            }
            if (montoRetirar == null || montoRetirar <= 0) {
                throw new Exception("El monto a retirar debe ser mayor a 0.");
            }

            Cliente clienteTienda = clienteHelper.obtenerCliente(ID_CLIENTE_TIENDA);
            if (clienteTienda == null) {
                throw new Exception("Error crítico: El cliente marcador '" + ID_CLIENTE_TIENDA + "' no existe.");
            }

            clienteTienda.setFechaRegistro(new java.util.Date());

            clienteHelper.ModificarCliente(clienteTienda);

            Paga pagoRetiro = new Paga();
            pagoRetiro.setIdCliente(clienteTienda);
            pagoRetiro.setIdUsuariorecep(usuarioValidado.getIdUsuariorecep());
            pagoRetiro.setFecha(LocalDate.now());
            pagoRetiro.setMonto(montoRetirar * -1.0);
            pagoRetiro.setPorPagar((byte) 0);

            if (observaciones != null && !observaciones.trim().isEmpty()) {
                pagoRetiro.setObservaciones(observaciones.trim());
            } else {
                pagoRetiro.setObservaciones(observaciones);
            }

            pagaHelper.RealizarPago(pagoRetiro, ID_ITEM_RETIRO);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Retiro de $" + montoRetirar + " registrado."));
            PrimeFaces.current().ajax().update("formPagos:tablaPagos");
            PrimeFaces.current().executeScript("PF('dlgMontoRetiro').hide(); PF('dlgConfirmacionRetiro').show();");

            // Limpiar campos
            limpiar();

        } catch (Exception e) {
            fc.addMessage("msgsMontoRetiro",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Registrar", e.getMessage()));
            e.printStackTrace();
        }
    }

    public void limpiar() {
        this.idUsuarioRecep = null;
        this.contrasenaUR = null;
        this.montoRetirar = null;
        this.usuarioValidado = null;
        this.observaciones = null;
    }

    // getters y setters
    public String getIdUsuarioRecep() { return idUsuarioRecep; }
    public void setIdUsuarioRecep(String idUsuarioRecep) { this.idUsuarioRecep = idUsuarioRecep; }

    public Double getMontoRetirar() { return montoRetirar; }
    public void setMontoRetirar(Double montoRetirar) { this.montoRetirar = montoRetirar; }

    public String getContrasenaUR() { return contrasenaUR; }
    public void setContrasenaUR(String contrasenaUR) { this.contrasenaUR = contrasenaUR; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}