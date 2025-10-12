package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Clase;
import java.util.List;
import java.util.Optional;

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
            e.printStackTrace();
        }
    }

    //Aqui se genera el nuevo ID
    public String generarNuevoIdClase() {
        return Clase.generarNuevoId();
    }

    public boolean eliminarClase(String idClase){
        EntityTransaction et = null;
        boolean eliminado = false;

        try{
            et = em.getTransaction();
            et.begin();//Se inicializa la transaccion

            Clase clase = em.find(Clase.class, idClase);//Encuentra el id de la clase

            if(clase != null){
                if(!em.contains(clase)){
                    clase = em.merge(clase);
                }
                em.remove(clase);
                eliminado = true;//Se confirma la eliminacion
            }
            et.commit();//Se manda lo realizado
            return eliminado;

        } catch (Exception e){
            if(et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        }
        return eliminado;
    }
    public Clase buscarClasePorId(String id) {
        try {
            return em.find(Clase.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la clase por ID", e);
        }
    }

    public List<Clase> listarTodasLasClases() {
        return findAll();
    }

    public void actualizarClase(Clase cla) {
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();

            // Iniciar transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualizar la clase existente
            em.merge(cla);

            // Confirmar los cambios
            tx.commit();

        } catch (Exception e) {
            // Revertir la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar la clase.", e);
        }
    }
}
