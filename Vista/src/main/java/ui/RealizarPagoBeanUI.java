package ui;

import helper.PagaHelper;
import helper.UsuarioRHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Paga;
import mx.desarollo.entity.Usuariorecepcionista;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Named("RealizarPagoBeanUI")
@SessionScoped
public class RealizarPagoBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double montoTotal = 0.0;
    private Double montoIngresado = 0.0;
    private Double montoFaltante = 0.0;
    private Double montoVenta = 0.0;

    private Paga paga = new Paga();
    private Cliente cliente;
    private Date fecha;
    private Double monto;
    private byte porPagar;

    private String idUR;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioRecepcionista;

    private final PagaHelper pagaHelper = new PagaHelper();
    private final UsuarioRHelper usuarioHelper = new UsuarioRHelper();

    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (idUR == null || idUR.trim().isEmpty())
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");

            usuarioRecepcionista = usuarioHelper.obtenerUsuarioR(idUR.trim());
            if (usuarioRecepcionista == null)
                throw new Exception("No se encontró un usuario con ese ID.");

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario verificado", "Recepcionista encontrado."));
        } catch (Exception e) {
            usuarioRecepcionista = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar usuario", e.getMessage()));
        }
    }

    public void validarContrasena() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe verificar primero al usuario recepcionista antes de validar la contraseña.");

            if (contrasenaUR == null || contrasenaUR.trim().isEmpty())
                throw new Exception("Debe ingresar la contraseña del recepcionista.");

            if (!usuarioRecepcionista.getContrasena().equals(contrasenaUR)) {
                fc.validationFailed();
                throw new Exception("Contraseña incorrecta.");
            }

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso autorizado", "El recepcionista ha sido autenticado correctamente."));
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", e.getMessage()));
        }
    }

    private void calcularFaltante() {
        if (montoIngresado == null) montoIngresado = 0.0;
        if (montoTotal == null) montoTotal = 0.0;
        montoFaltante = Math.max(montoTotal - montoIngresado, 0.0);
    }

    public void realizarPagoInteractivo() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {

            String tipo = obtenerTotal("membresia");

            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            if (montoTotal <= 0)
                throw new Exception("Monto total inválido. Seleccione un tipo de pago o calcule el total.");

            if (montoIngresado == null || montoIngresado <= 0)
                throw new Exception("Debe ingresar un monto para continuar.");

            calcularFaltante();

            if (montoIngresado < montoTotal) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Monto insuficiente", "Faltan " + montoFaltante + " pesos."));
                return;
            }

            if (paga == null) paga = new Paga();

            if (cliente == null) {
                cliente = new Cliente();
                cliente.setIdCliente("CLI0002");
            }

            paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
            paga.setFecha(LocalDate.now());
            paga.setIdCliente(cliente);
            paga.setMonto(montoTotal);
            paga.setPorPagar(porPagar);

            pagaHelper.RealizarPago(paga, tipo);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago exitoso", "Se ha recibido el pago completo."));

            limpiarCampos();
            montoIngresado = 0.0;
            montoFaltante = 0.0;

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al realizar el pago", e.getMessage()));
        }
    }

    public void realizarPagoTarjeta() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            if (paga == null) paga = new Paga();

            if (cliente == null) {
                cliente = new Cliente();
                cliente.setIdCliente("CLI0002");
            }

            String tipo = obtenerTotal("membresia");

            paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
            paga.setFecha(LocalDate.now());
            paga.setIdCliente(cliente);
            paga.setMonto(montoTotal);
            paga.setPorPagar(porPagar);

            pagaHelper.RealizarPago(paga, tipo);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago con tarjeta", "El pago con tarjeta fue procesado correctamente."));
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al realizar el pago con tarjeta", e.getMessage()));
        }
    }

    public void finalizarProcesoTarjeta() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago completado", "El pago con tarjeta ha sido procesado correctamente."));
            limpiarCampos();
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al finalizar", e.getMessage()));
        }
    }

    private void limpiarCampos() {
        cliente = null;
        monto = null;
        fecha = null;
        porPagar = 0;
        contrasenaUR = null;
        idUR = null;
        usuarioRecepcionista = null;
        paga = new Paga();
        montoTotal = 0.0;
        montoIngresado = 0.0;
        montoFaltante = 0.0;
    }

    private String obtenerTotal(String tipo) {
        if (tipo == null) tipo = "";
        tipo = tipo.toLowerCase();

        switch (tipo) {
            case "membresia":
                montoTotal = 800.00;
                break;
            case "clase":
                montoTotal = 500.00;
                break;
            case "venta":
                montoTotal = (montoVenta != null && montoVenta > 0) ? montoVenta : 0.0;
                break;
            default:
                montoTotal = 0.0;
                break;
        }

        calcularFaltante();
        return tipo;
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public byte getPorPagar() { return porPagar; }
    public void setPorPagar(byte porPagar) { this.porPagar = porPagar; }

    public String getIdUR() { return idUR; }
    public void setIdUR(String idUR) { this.idUR = idUR; }

    public String getContrasenaUR() { return contrasenaUR; }
    public void setContrasenaUR(String contrasenaUR) { this.contrasenaUR = contrasenaUR; }

    public Usuariorecepcionista getUsuarioRecepcionista() { return usuarioRecepcionista; }

    public Double getMontoTotal() { return montoTotal; }
    public Double getMontoIngresado() { return montoIngresado; }

    public void setMontoIngresado(Double montoIngresado) {
        this.montoIngresado = montoIngresado;
        calcularFaltante();
    }

    public Double getMontoFaltante() { return montoFaltante; }
}
