package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClaseDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Clase;

public class ClaseDelegate {
    private final ClaseDAO claseDAO;

    public ClaseDelegate() {
        this.claseDAO = ServiceLocator.getInstanceClaseDAO();
    }

    public void registrarClase(Clase clase) throws Exception {
        if (clase.getNombre() == null || clase.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la clase esta vacio");
        }
        if (clase.getHorario() == null || clase.getHorario().trim().isEmpty()) {
            throw new Exception("El horario de la clase esta vacio");
        }
        if (clase.getMaestro() == null || clase.getMaestro().trim().isEmpty()) {
            throw new Exception("El nombre del maestro esta vacio");
        }
        if (clase.getCupoMaximo() <= 0) {
            throw new Exception("El cupo maximo debe ser mayor que cero");
        }

        //Se llama al metodo para asignar y crear un nuevoID
        clase.setIdItem(claseDAO.generarNuevoIdClase());

        claseDAO.crear(clase);
    }
}
