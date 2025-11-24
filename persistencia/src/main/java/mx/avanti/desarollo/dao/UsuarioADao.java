package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Usuarioadministrador;

import java.util.List;

public class UsuarioADao extends AbstractDAO<Usuarioadministrador> {

    private final EntityManager em;
    private static boolean contadorInicializado = false;

    public UsuarioADao(EntityManager em) {
        super(Usuarioadministrador.class);
        this.em = em;
        if (!contadorInicializado) {
            sincronizarContador();
            contadorInicializado = true;
        }
    }

    /**
     * Metodo para registrar un Usuario administrador
     * @Throws Si la base de datos rechaza el registro
     * @Params Un objeto de Usuarioadministrador
     * @return void
     */
    public void crearUsuarioA(Usuarioadministrador ua) {
        EntityTransaction tx = null;
        try {
            sincronizarContador();
            ua.setEstatus(1);

            if (ua.getIdUsuarioadmin() == null || ua.getIdUsuarioadmin().isEmpty()) {
                ua.setIdUsuarioadmin(Usuarioadministrador.generarNuevoId());
            }

            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }

            em.persist(ua);
            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el Usuario ", e);
        }
    }

    /**
     * Metodo para sincronizar el contador de la generacion del ID del usuarioadministrador
     * @Throws Si en la base de datos no hay usuarios administradores registrados
     * @return void
     */
    private void sincronizarContador() {
        try {
            String ultimoId = em
                    .createQuery("SELECT ua.idUsuarioadmin FROM Usuarioadministrador ua WHERE ua.idUsuarioadmin LIKE 'UA%' ORDER BY ua.idUsuarioadmin DESC", String.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (ultimoId != null && ultimoId.startsWith("UA")) {
                int numero = Integer.parseInt(ultimoId.substring(2));
                Usuarioadministrador.setContador(numero + 1);
                System.out.println("Contador sincronizado con base de datos: siguiente UA" + (numero + 1));
            }
        } catch (NoResultException e) {
            Usuarioadministrador.setContador(1000);
            System.out.println("No hay usuarios administradores registrados. Contador iniciado en UA1000");
        } catch (Exception e) {
            Usuarioadministrador.setContador(1000);
            System.err.println("Error sincronizando contador, se mantiene en UA1000: " + e.getMessage());
        }
    }

    public List<Usuarioadministrador> findAllWithUsuarioA() {
        return execute(em -> {
            em.clear();

            List<Usuarioadministrador> result = em.createQuery(
                            "SELECT Ua FROM Usuarioadministrador Ua WHERE Ua.estatus = 1",
                            Usuarioadministrador.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }
    public List<Usuarioadministrador> listarActivos() {
        return execute(em -> {
            em.clear();
            return em.createQuery("SELECT u FROM Usuarioadministrador u WHERE u.estatus = 1", Usuarioadministrador.class).getResultList();
        });
    }

    public Usuarioadministrador buscarADMPorId(String id) {
        try {
            return em.find(Usuarioadministrador.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el Usuario Administrador por ID", e);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}