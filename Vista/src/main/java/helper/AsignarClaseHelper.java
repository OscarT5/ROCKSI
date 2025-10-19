package helper;

import mx.avanti.desarollo.dao.AsignacionDAO;

public class AsignarClaseHelper {

    private final AsignacionDAO dao = new AsignacionDAO();

    public void asignarClaseACliente(String idCliente, String idClase) throws Exception {
        dao.asignarClaseACliente(idCliente, idClase);
    }
}
