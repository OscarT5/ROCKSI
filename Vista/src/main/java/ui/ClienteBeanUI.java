package ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.List;
import mx.desarollo.entity.Cliente;
import helper.ClienteHelper;

import jakarta.faces.view.ViewScoped;
import java.io.Serializable;

@Named("clienteBeanUI")
@ViewScoped
public class ClienteBeanUI implements Serializable {
    private List<Cliente> listaClientes;
    private ClienteHelper clienteHelper = new ClienteHelper();

    @PostConstruct
    public void init() {
        try {
            listaClientes = clienteHelper.ObtenerClientes();
            System.out.println("Clientes cargados: " + (listaClientes != null ? listaClientes.size() : 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }
}


