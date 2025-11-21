package ui;

import helper.LoginHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Usuarioadministrador;
import mx.desarollo.entity.Usuariorecepcionista;

import java.io.Serializable;

@Named("loginBeanUI")
@SessionScoped
public class LoginBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;
    private final LoginHelper loginHelper = new LoginHelper();

    private String idUsuario;
    private String contrasena;
    private Object usuarioLogueado;
    private String tipoUsuario;

    public String iniciarSesion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            Object usuarioEncontrado = loginHelper.autenticarUsuario(idUsuario, contrasena);

            this.usuarioLogueado = usuarioEncontrado;

            fc.getExternalContext().getSessionMap().put("USUARIO_ACTUAL", usuarioEncontrado);

            if (usuarioEncontrado instanceof Usuarioadministrador) {
                this.tipoUsuario = "ADMIN";
                return "/home.xhtml?faces-redirect=true";
            } else if (usuarioEncontrado instanceof Usuariorecepcionista) {
                this.tipoUsuario = "RECEPCIONISTA";
                return "/home.xhtml?faces-redirect=true";
            }

        } catch (Exception e) {
            this.usuarioLogueado = null;
            this.contrasena = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de acceso", e.getMessage()));
        }

        return null;
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Object getUsuarioLogueado() { return usuarioLogueado; }
    public String getTipoUsuario() { return tipoUsuario; }
}