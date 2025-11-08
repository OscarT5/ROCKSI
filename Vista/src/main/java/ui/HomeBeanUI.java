package ui;

import helper.ClienteHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import mx.desarollo.entity.Cliente;
import helper.InventarioDiarioHelper;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;


@Named("homeBeanUI")
@SessionScoped
public class HomeBeanUI implements Serializable {

    private List<Cliente> listaClientes;

    @Inject
    private ClienteHelper clienteHelper;
    private final InventarioDiarioHelper inventarioHelper = new InventarioDiarioHelper();

    @PostConstruct
    public void init() {
        try {
            listaClientes = clienteHelper.ObtenerClientes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarDia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // 2. Llama al helper
            inventarioHelper.ejecutarSnapshotDiario();

            // 3. Mensaje de éxito
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Día Iniciado", "El inventario inicial se ha guardado correctamente."));

        } catch (Exception e) {
            // 4. Mensaje de error (ej. "El snapshot... ya fue generado.")
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al iniciar día", e.getMessage()));
        }
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }
}
