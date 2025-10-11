package mx.desarollo.integration;

import mx.desarollo.facade.ClaseFacade;
import mx.desarollo.facade.ClienteFacade;

public class ServiceFacadeLocator {

    private static ClienteFacade ClienteFacade;
    private static ClaseFacade ClaseFacade;
    //private static FacadeUsuario facadeUsuario;

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

    /*
    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
            return facadeUsuario;
        } else {
            return facadeUsuario;
        }
    }
    
     */
}
