package ui;

import helper.ClienteHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("pruebaClienteBeanUI")
@RequestScoped
public class PruebaClienteBeanUI {

    @PostConstruct
    public void init() {
        ClienteHelper helper = new ClienteHelper();
        helper.pruebaEliminarCliente();
    }
}
