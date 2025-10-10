package ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.List;
import mx.desarollo.entity.Cliente;
import helper.ClienteHelper;

@Named("clienteBeanUI")
@RequestScoped
public class ClienteBeanUI {
    private List<Cliente> listaClientes;
    private ClienteHelper clienteHelper = new ClienteHelper();

    @PostConstruct
    public void init() {
        try {
            //listaClientes = clienteHelper.obtenerClientes(); // método en el helper que llama al DAO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }
}

