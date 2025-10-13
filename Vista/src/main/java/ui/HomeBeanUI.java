package ui;

import helper.ClienteHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import mx.desarollo.entity.Cliente;

//en este bean se precarga la bd y no tenga que haber una primera interaccion tardada
@Named("homeBeanUI")
@SessionScoped
public class HomeBeanUI implements Serializable {

    private List<Cliente> listaClientes;

    private final ClienteHelper clienteHelper = new ClienteHelper();

    @PostConstruct
    public void init() {
        try {
            // cargar todos los datos al iniciar la vista
            listaClientes = clienteHelper.ObtenerClientes();
            System.out.println("Datos precargados correctamente al iniciar Home.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error precargando datos: " + e.getMessage());
        }
    }

    public List<Cliente> getListaClientes() { return listaClientes; }
}
