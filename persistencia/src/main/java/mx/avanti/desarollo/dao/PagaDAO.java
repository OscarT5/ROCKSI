package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Item;
import mx.desarollo.entity.Paga;

import java.time.LocalDate;
import java.util.List;

public class PagaDAO extends AbstractDAO<Paga> {
    private final EntityManager em;
    private static boolean contadorInicializado = false;

    // Constructor
    public PagaDAO(EntityManager em) {
        super(Paga.class);
        this.em = em;
        if (!contadorInicializado) {
            sincronizarContador();
            contadorInicializado = true;
        }
    }

    /**
     * Metodo para obtener todas las pagas registradas
     * @Throws Si la base de datos rechaza la peticion de obtener todas las pagas
     * @return Una lista de pagas
     */
    public List<Paga> findAllWithPaga() {
        return execute(em -> {
            //Limpia contexto antes de ejecutar la query
            em.clear();

            List<Paga> result = em.createQuery(
                            "SELECT DISTINCT p FROM Paga p LEFT JOIN FETCH p.idCliente ORDER BY p.idPaga ASC",
                            Paga.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH") //forzar lectura desde la BD
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }
    
    public Item findItemById(String idItem) {
        try {
            return em.find(Item.class, idItem);
        } catch (Exception e) {
            System.err.println("Error al buscar Item por ID (" + idItem + "): " + e.getMessage());
            return null;
        }
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Metodo para crear una paga
     * @Throws Si la base de datos rechaza la peticion de crear el pago
     * @Params Un objeto de tipo Paga
     * @return void
     */
    public void crear(Paga paga){
        save(paga);
        sincronizarContador();
    }

    /**
     * Metodo para sincrinizar el numero de pago con el utlimo pago realizado para crear el ID de la paga
     * @Throws Si no hay ningun pago registrado en la base de datos
     * @return void
     */
    private void sincronizarContador() {
        try {
            String ultimoId = em
                    .createQuery("SELECT p.idPaga FROM Paga p WHERE p.idPaga LIKE 'PA%' ORDER BY p.idPaga DESC", String.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (ultimoId != null && ultimoId.startsWith("PA")) {
                int numero = Integer.parseInt(ultimoId.substring(2));
                Paga.setContador(numero + 1);
                System.out.println("Contador de pagos sincronizado: siguiente P" + (numero + 1));
            }
        } catch (NoResultException e) {
            Paga.setContador(1000);
            System.out.println("ℹNo hay pagos registrados. Contador iniciado en P1000.");
        } catch (Exception e) {
            Paga.setContador(1000);
            System.err.println("Error al sincronizar el contador de pagos, se mantiene en P1000: " + e.getMessage());
        }
    }

    /**
     * Metodo para generar un nuevo ID de paga
     * @return Un objeto de tipo String id de la paga (Ej.PA####)
     */
    public String generarNuevoIdPaga() {
        return Paga.generarNuevoId();
    }

    /**
     * Metodo para eliminar una paga
     * @Throws Si la base de datos rechaza la peticion de eliminar la paga por ID o no se encuentra la paga con el ID
     * @Params Un objeto de tipo String id de la paga
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarPaga(String idPaga){
        EntityTransaction et = null;
        boolean eliminado = false;

        try{
            et = em.getTransaction();
            et.begin();//Se inicializa la transaccion

            Paga paga = em.find(Paga.class, idPaga);//Encuentra el id del Pago

            if(paga != null){
                if(!em.contains(paga)){
                    paga = em.merge(paga);
                }
                em.remove(paga);
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

    public List<Paga> buscarPagosPorId(String idParcial) {
        return execute(em -> {
            em.clear();
            return em.createQuery(
                            "SELECT p FROM Paga p " +
                                    "JOIN FETCH p.idCliente c " +
                                    "JOIN FETCH p.idItem i " +
                                    "WHERE p.idPaga LIKE :filtro", Paga.class)
                    .setParameter("filtro", "%" + idParcial + "%")
                    .getResultList();
        });
    }

    /*public List<Clase> listarTodasLasClases() {
        return findAll();
    }
     */

    /**
     * Metodo para actualizar una paga
     * @Throws Si la base de datos rechaza la peticion de actualizar la paga
     * @Params Un objeto de tipo Paga
     * @return void
     */
    public void actualizarPaga(Paga paga) {
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();

            // Inicia la transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualiza el pago existente
            em.merge(paga);

            // Confirma los cambios
            tx.commit();

        } catch (Exception e) {
            // Revierte la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar el pago.", e);
        }
    }
    
    public List<Paga> findByFecha(LocalDate fecha) {
        return execute(em -> {
            em.clear();
            return em.createQuery(
                            "SELECT p FROM Paga p " +
                                    "JOIN FETCH p.idCliente c " +
                                    "JOIN FETCH p.idItem i " +
                                    "WHERE p.fecha = :fecha", Paga.class)
                    .setParameter("fecha", fecha)
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        });
    }
    
    public List<Paga> findByFechaBetween(LocalDate inicioMes, LocalDate finMes) {
        return execute(em -> {
            em.clear();
            return em.createQuery(
                            "SELECT p FROM Paga p " +
                                    "JOIN FETCH p.idItem i " +   // Trae el Item (Producto/Membresia)
                                    "WHERE p.fecha >= :inicio AND p.fecha <= :fin", Paga.class)
                    .setParameter("inicio", inicioMes)
                    .setParameter("fin", finMes)
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        });
    }

}
