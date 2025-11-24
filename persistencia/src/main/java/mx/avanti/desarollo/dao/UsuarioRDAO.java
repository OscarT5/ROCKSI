package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Usuariorecepcionista;

import java.util.List;

public class UsuarioRDAO extends AbstractDAO<Usuariorecepcionista> {
    private final EntityManager em;
    private static boolean contadorInicializado = false;

    public UsuarioRDAO(EntityManager em) {
        super(Usuariorecepcionista.class);
        this.em = em;
        if (!contadorInicializado) {
            sincronizarContador();
            contadorInicializado = true;
        }
    }

    /**
     * Metodo para registrar un Usuario recepcionista
     * @Throws Si la base de datos rechaza el registro
     * @Params Un objeto de Usuariorecepcionista
     * @return void
     */
    public void crearUsuarioR(Usuariorecepcionista ur) {
        EntityTransaction tx = null;
        try {
            sincronizarContador();
            ur.setEstatus(1);

            if (ur.getIdUsuariorecep() == null || ur.getIdUsuariorecep().isEmpty()) {
                ur.setIdUsuariorecep(Usuariorecepcionista.generarNuevoId());
            }

            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }

            em.persist(ur);
            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el Usuario ", e);
        }
    }

    /**
     * Metodo para sincronizar el contador de la generacion del ID del usuario recepcionista
     * @Throws Si en la base de datos no hay usuarios recepcionistas registrados
     * @return void
     */
    private void sincronizarContador() {
        try {
            String ultimoId = em
                    .createQuery("SELECT ur.idUsuariorecep FROM Usuariorecepcionista ur WHERE ur.idUsuariorecep LIKE 'UR%' ORDER BY ur.idUsuariorecep DESC", String.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (ultimoId != null && ultimoId.startsWith("UR")) {
                int numero = Integer.parseInt(ultimoId.substring(2));
                Usuariorecepcionista.setContador(numero + 1);
                System.out.println("Contador sincronizado con base de datos: siguiente UR" + (numero + 1));
            }
        } catch (NoResultException e) {
            Usuariorecepcionista.setContador(1000);
            System.out.println("No hay usuarios recepcionistas registrados. Contador iniciado en UR1000");
        } catch (Exception e) {
            Usuariorecepcionista.setContador(1000);
            System.err.println("Error sincronizando contador, se mantiene en UR1000: " + e.getMessage());
        }
    }

    public List<Usuariorecepcionista> findAllWithUsuarioR() {
        return execute(em -> {
            em.clear();

            List<Usuariorecepcionista> result = em.createQuery(
                            "SELECT Ur FROM Usuariorecepcionista Ur WHERE Ur.estatus = 1",
                            Usuariorecepcionista.class
                    )
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .setHint("org.hibernate.cacheable", false)
                    .getResultList();

            return result;
        });
    }

    public void actualizar(Usuariorecepcionista usuario) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            em.merge(usuario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el usuario recepcionista en la BD", e);
        }
    }

    public boolean baja(String id) {
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }

            Usuariorecepcionista usuario = buscarURPorId(id);

            if (usuario == null) {
                if (tx.isActive()) tx.rollback();
                return false;
            }

            usuario.setEstatus(0);
            em.merge(usuario);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al dar de baja al usuario recepcionista", e);
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public Usuariorecepcionista buscarURPorId(String id) {
        try {
            return em.find(Usuariorecepcionista.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el Usuario recepcionista por ID", e);
        }
    }

}
