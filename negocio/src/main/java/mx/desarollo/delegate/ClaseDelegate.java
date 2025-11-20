package mx.desarollo.delegate;

import mx.avanti.desarollo.dao.ClaseDAO;
import mx.avanti.desarollo.integration.ServiceLocator;
import mx.desarollo.entity.Clase;

import java.util.List;

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

    public boolean eliminarClase(String idClase) throws Exception {
        if(idClase == null || idClase.trim().isEmpty()) {
            throw new Exception("El id del clase esta vacio");
        }
        return claseDAO.eliminarClase(idClase);
    }

    /*
    este metodo sirve para actualizar clientes mediante su ID
    que esta es proporcionada por el bean, falta implementar el bean
     */
    public void actualizarClase(Clase cla) throws Exception {
        String idAActualizar = cla.getIdClase();
        if (idAActualizar == null || idAActualizar.trim().isEmpty()) {
            throw new Exception("No se proporcionó ID de clase para modificar.");
        }

        Clase existente = claseDAO.buscarClasePorId(idAActualizar);
        if (existente == null) {
            throw new Exception("No existe la clase con ID " + idAActualizar + " en la base de datos.");
        }

        // Validaciones
        if (cla.getNombre() == null || cla.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        if (cla.getHorario() == null || cla.getHorario().trim().isEmpty()) {
            throw new Exception("El horario no puede estar vacío.");
        }

        String horario = cla.getHorario().trim();

        if (!horario.matches("^([01]?\\d|2[0-3]):[0-5]\\d\\s*[-aA]\\s*([01]?\\d|2[0-3]):[0-5]\\d$")) {
            throw new Exception("Horario inválido. Use formato como '08:00 - 16:00' o '9:00 a 17:30'.");
        }
        if (cla.getMaestro() == null || cla.getMaestro().trim().isEmpty()) {
            throw new Exception("El nombre del maestro que imparte la clase no puede estar vacío.");
        }
        if (cla.getCupoMaximo() <= 0) {
            throw new Exception("El cupo máximo debe ser un número mayor que cero.");
        }

        // Aplicar cambios
        existente.setNombre(cla.getNombre());
        existente.setMaestro(cla.getMaestro());
        existente.setCupoMaximo(cla.getCupoMaximo());
        existente.setHorario(horario);

        claseDAO.actualizarClase(existente);
    }

    /**
     * Metodo para obtener una clase por su ID que llamara a la instancia de ClaseDAO
     * @Throws Si la base de datos rechaza la peticion o no se encuntra la clase con el ID
     * @Params Un String id de la clase
     * @return Un objeto de tipo Clase
     */
    public Clase obtenerClase(String id) {
        try {
            // Si el String id esta vacio entonces
            if (id == null) return null; // retorna null
            id = id.trim();
            // Si el String id esta limpio
            if (id.isEmpty()) return null; // retorna null

            // Si id contiene algun dato busca la clase por el id con el metodo .find(id) y se la asigna a un objeto de tipo Clase
            Clase cla = claseDAO.find(id).orElse(null);

            // Retorna la clase
            return cla;
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo la clase con id=" + id, e);
        }
    }

    public List<Clase> listarClases() {
        return claseDAO.findAllWithClientes();
    }

}
