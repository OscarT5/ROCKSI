package mx.desarollo.integration;

import mx.desarollo.facade.*;

public class ServiceFacadeLocator {

    private static ClienteFacade ClienteFacade;
    private static ClaseFacade ClaseFacade;
    private static ProductoFacade ProductoFacade;
    private static PagaFacade PagaFacade;
    private static UsuarioRFacade UsuarioRFacade;


    public static ClienteFacade getInstanceClienteFacade() {
        if (ClienteFacade == null) {
            ClienteFacade = new ClienteFacade();
            return ClienteFacade;
        } else {
            return ClienteFacade;
        }
    }
    public static ClaseFacade getInstanceClaseFacade() {
        if(ClaseFacade == null){
            ClaseFacade = new ClaseFacade();
            return ClaseFacade;
        }
        else{
            return ClaseFacade;
        }
    }
    public static ProductoFacade getInstanceProductoFacade() {
        if(ProductoFacade == null){
            ProductoFacade = new ProductoFacade();
            return ProductoFacade;
        }
        else{
            return ProductoFacade;
        }
    }
    public static PagaFacade getInstancePagaFacade() {
        if(PagaFacade == null){
            PagaFacade =  new PagaFacade();
            return PagaFacade;
        }
        else{
            return PagaFacade;
        }
    }
    public static UsuarioRFacade getInstanceURFacade() {
        if(UsuarioRFacade == null){
            UsuarioRFacade =  new UsuarioRFacade();
            return UsuarioRFacade;
        }
        else{
            return UsuarioRFacade;
        }
    }

}
