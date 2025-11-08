package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Paga;

import java.time.LocalDate;
import java.util.List;

public class PagaDAO extends AbstractDAO<Paga> {
    private final EntityManager em;
    private static boolean contadorInicializado = false;

    public PagaDAO(EntityManager em) {
        super(Paga.class);
        this.em = em;
        if (!contadorInicializado) {
            sincronizarContador();
            contadorInicializado = true;
        }
    }

    public List<Paga> findAllWithPaga() {
        return execute(em -> {
            //Limpia contexto antes de ejecutar la query
            em.clear();

            List<Paga> result = em.createQuery(
                            "SELECT DISTINCT p FROM Paga p LEFT JOIN FETCH p.idCliente",
                            Paga.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH") //forzar lectura desde la BD
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void crear(Paga paga){
        save(paga);
        sincronizarContador();
    }

    /*
    En esta funcion se inicializa el contador para su respectivo ID que empieza con CLI
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

    //Aqui se genera el nuevo ID
    public String generarNuevoIdPaga() {
        return Paga.generarNuevoId();
    }

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

    public Paga buscarPagaPorId(String id) {
        try {
            return em.find(Paga.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el Pago por ID", e);
        }
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

    public void actualizarPaga(Paga paga) {
        EntityTransaction tx = null;

        try {
            tx = em.getTransaction();

            // Iniciar transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualizar el pago existente
            em.merge(paga);

            // Confirmar los cambios
            tx.commit();

        } catch (Exception e) {
            // Revertir la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar el pago.", e);
        }
    }

    //Esta funcion busca los pagos de una fecha especifica para el reporte diario, trae info del cliente y del item
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

}
