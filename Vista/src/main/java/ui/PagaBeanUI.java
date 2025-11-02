package ui;

import helper.PagaHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Paga;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("pagoBeanUI")
@SessionScoped
public class PagaBeanUI implements Serializable {

    private List<Paga> listaPagos;
    private String filtro;

    private final PagaHelper pagaHelper = new PagaHelper();

    @PostConstruct
    public void init() {
        cargarPagos();
    }

    public void recargar() {
        cargarPagos();
    }

    public void filtrarPorId() {
        try {
            if (filtro == null || filtro.trim().isEmpty()) {
                recargar();
            } else {
                listaPagos = pagaHelper.buscarPagosPorId(filtro.trim());
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al filtrar pagos."));
        }
        PrimeFaces.current().ajax().update("formPagos:tablaPagos");
    }

    public void cargarPagos() {
        try {
            listaPagos = pagaHelper.listarPagos();
            if (listaPagos == null || listaPagos.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "No hay pagos registrados."));
            }
        } catch (Exception e) {
            listaPagos = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar los pagos."));
            e.printStackTrace();
        }
    }

    // metodos placeholders para acciones de la caja
    public void aceptarPagoAdelantado() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pago adelantado aceptado."));
    }

    public void inicioCorte() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inicio de corte registrado."));
    }

    public void tomarDineroCaja() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dinero tomado de la caja."));
    }

    // Getters y setters
    public List<Paga> getListaPagos() {
        return listaPagos;
    }

    public void setListaPagos(List<Paga> listaPagos) {
        this.listaPagos = listaPagos;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
}
