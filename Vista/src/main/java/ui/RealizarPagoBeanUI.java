package ui;

import helper.PagaHelper;
import helper.UsuarioRHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Item;
import mx.desarollo.entity.Paga;
import mx.desarollo.entity.Usuariorecepcionista;

import java.io.Serializable;
import java.util.Date;

@Named("RealizarPagoBeanUI")
@SessionScoped
public class RealizarPagoBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    //Datos que el usuario va ingresando
    private Double montoTotal = 500.0;  // monto que se debe cobrar
    private Double montoIngresado = 0.0; // monto que el usuario va ingresando
    private Double montoFaltante = 500.0; // monto que falta


    // Datos del pago
    private Paga paga;
    private Cliente cliente;
    private Item item;
    private Date fecha;
    private Double monto;
    private byte porPagar;

    // Datos del usuario recepcionista
    private String idUR;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioRecepcionista;

    // Helpers
    private final PagaHelper pagaHelper = new PagaHelper();
    private final UsuarioRHelper usuarioHelper = new UsuarioRHelper();

    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (idUR == null || idUR.trim().isEmpty()) {
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");
            }

            // Consultar si el usuario existe y es recepcionista
            usuarioRecepcionista = usuarioHelper.obtenerUsuarioR(idUR.trim());

            if (usuarioRecepcionista == null) {
                fc.validationFailed();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró un usuario con ese ID."));
                return;
            }

        } catch (Exception e) {
            usuarioRecepcionista = null;
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar usuario", e.getMessage()));
        }
    }

    public void validarContrasena() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // Validar que haya un usuario recepcionista cargado
            if (usuarioRecepcionista == null) {
                throw new Exception("Debe verificar primero al usuario recepcionista antes de validar la contraseña.");
            }

            // Validar que se haya ingresado una contraseña
            if (contrasenaUR == null || contrasenaUR.trim().isEmpty()) {
                throw new Exception("Debe ingresar la contraseña del recepcionista.");
            }

            // Comparar contraseñas
            if (!usuarioRecepcionista.getContrasena().equals(contrasenaUR)) {
                fc.validationFailed();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contrasena incorrecta"));
                return;
            }

            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Acceso autorizado",
                    "El recepcionista ha sido autenticado correctamente. Puede realizar el pago."
            ));

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error de autenticación",
                    e.getMessage()
            ));
        }
    }

    private void calcularFaltante() {
        if (montoIngresado == null) montoIngresado = 0.0;
        montoFaltante = montoTotal - montoIngresado;
        if (montoFaltante < 0) montoFaltante = 0.0;
    }

    public void realizarPagoInteractivo() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (montoIngresado < montoTotal) {
                fc.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Monto insuficiente",
                        "Aún faltan " + montoFaltante + " pesos."
                ));
            } else {
                paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                paga.setMonto(montoTotal);
                paga.setPorPagar(porPagar);
                pagaHelper.RealizarPago(paga);

                fc.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Pago exitoso",
                        "Se ha recibido el pago completo."
                ));

                limpiarCampos();
                montoIngresado = 0.0;
                montoFaltante = montoTotal;
            }
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al realizar el pago", e.getMessage()));
        }
    }

    private void limpiarCampos() {
        cliente = null;
        item = null;
        monto = null;
        fecha = null;
        porPagar = 0;
        contrasenaUR = null;
        idUR = null;
        usuarioRecepcionista = null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public byte getPorPagar() {
        return porPagar;
    }

    public void setPorPagar(byte porPagar) {
        this.porPagar = porPagar;
    }

    public String getIdUR() {
        return idUR;
    }

    public void setIdUR(String idUR) {
        this.idUR = idUR;
    }

    public String getContrasenaUR() {
        return contrasenaUR;
    }

    public void setContrasenaUR(String contrasenaUR) {
        this.contrasenaUR = contrasenaUR;
    }

    public Usuariorecepcionista getUsuarioRecepcionista() {
        return usuarioRecepcionista;
    }

    public Double getMontoTotal() { return montoTotal; }

    public Double getMontoIngresado() { return montoIngresado; }

    public void setMontoIngresado(Double montoIngresado) {
        this.montoIngresado = montoIngresado;
        calcularFaltante();
    }
    public Double getMontoFaltante() { return montoFaltante; }


}
