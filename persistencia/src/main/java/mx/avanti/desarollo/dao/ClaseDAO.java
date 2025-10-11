package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Clase;
import java.util.List;

public class ClaseDAO extends AbstractDAO<Clase> {
    private final EntityManager em;

    public ClaseDAO(EntityManager em) {
        super(Clase.class);
        this.em = em;
        inicializarContador();//Se inicializa el contador para crear los IDS
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void crear(Clase clase){
        save(clase);
    }

    //Metodo para que el contador se inicialize y se cree asi un nuevo ID posteriormente
    private void inicializarContador() {
        try {
            //Se obtienen los ids de las clases que ya existen dentro de la BD
            List<String> ids = em
                    .createQuery("SELECT c.idItem FROM Clase c", String.class)
                    .getResultList();

            int max = 1000; //Valor inicial
            for (String id : ids) {
                if (id != null && id.startsWith("CLA")) {
                    try {
                        int n = Integer.parseInt(id.substring(3));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {}
                }
            }

            //Aqui el contador se actualiza a uno mas que el maximo
            Clase.setContador(max + 1);
        } catch (Exception e) {
        }
    }

    //Aqui se genera el nuevo ID
    public String generarNuevoIdClase() {
        return Clase.generarNuevoId();
    }
}
