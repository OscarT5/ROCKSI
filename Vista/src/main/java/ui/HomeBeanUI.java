package ui;

import helper.ClienteHelper;
import helper.InventarioDiarioHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.desarollo.entity.Cliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("homeBeanUI")
@SessionScoped
public class HomeBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ClienteHelper clienteHelper;

    private final InventarioDiarioHelper inventarioHelper = new InventarioDiarioHelper();

    private String idBusqueda;
    private List<Cliente> listaIngresos;

    @PostConstruct
    public void init() {
        this.listaIngresos = new ArrayList<>();
    }

    public void registrarEntrada() {
        FacesContext fc = FacesContext.getCurrentInstance();

        try {
            if (idBusqueda == null || idBusqueda.trim().isEmpty()) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Ingrese un ID de cliente."));
                return;
            }

            String idBuscado = idBusqueda.trim();

            Cliente clienteEncontrado = clienteHelper.obtenerCliente(idBuscado);

            if (clienteEncontrado != null) {

                boolean yaRegistrado = listaIngresos.stream()
                        .anyMatch(c -> c.getIdCliente().equals(clienteEncontrado.getIdCliente()));

                if (yaRegistrado) {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ya registrado",
                            "El cliente " + clienteEncontrado.getNombreCompleto() + " ya ha ingresado previamente."));

                    this.idBusqueda = "";
                    return;
                }
                listaIngresos.add(0, clienteEncontrado);

                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido",
                        clienteEncontrado.getNombreCompleto() + " ha registrado su entrada."));
                this.idBusqueda = "";
            } else {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No encontrado", "El ID ingresado no existe."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void iniciarDia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            inventarioHelper.ejecutarSnapshotDiario();

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Día Iniciado", "El inventario inicial se ha guardado correctamente."));

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al iniciar día", e.getMessage()));
        }
    }
    public String getIdBusqueda() {
        return idBusqueda;
    }

    public void setIdBusqueda(String idBusqueda) {
        this.idBusqueda = idBusqueda;
    }

    public List<Cliente> getListaIngresos() {
        return listaIngresos;
    }

    public void setListaIngresos(List<Cliente> listaIngresos) {
        this.listaIngresos = listaIngresos;
    }
}