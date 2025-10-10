package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Cliente;

import java.util.List;
import java.util.Optional;

public class ClienteDAO extends AbstractDAO<Cliente> {

    private final EntityManager entityManager;

    public ClienteDAO(EntityManager em) {
        super(Cliente.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void crear(Cliente cliente) {
        EntityTransaction tx = null;
        try {
            inicializarContador();

            if (cliente.getIdCliente() == null || cliente.getIdCliente().isEmpty()) {
                cliente.setIdCliente(Cliente.generarNuevoId());
            }

            tx = entityManager.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }

            entityManager.persist(cliente);
            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear el cliente", e);
        }
    }

    /*
    En esta funcion se inicializa el contador para su respectivo ID que empieza con CLI
     */
    private void inicializarContador() {
        try {
            List<String> ids = entityManager
                    .createQuery("SELECT c.idCliente FROM Cliente c", String.class)
                    .getResultList();

            int max = 1000;
            for (String id : ids) {
                if (id != null && id.startsWith("CLI")) {
                    try {
                        int n = Integer.parseInt(id.substring(3));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {}
                }
            }

            Cliente.setContador(max + 1);
        } catch (Exception e) {
            // Si falla se deja el contador por defecto
        }
    }

    /*public Cliente buscarPorId(int id) {
        Optional<Cliente> opt = find(id);
        return opt.orElse(null);
    }

    public List<Cliente> listarTodos() {
        return findAll();
    }

    public void eliminar(int id) {
        Optional<Cliente> opt = find(id);
        opt.ifPresent(this::delete);
    }
    public void actualizar(Cliente cliente) {
        update(cliente);
    }

    public Cliente buscarPorTelefono(String Telefono) {
        List<Cliente> resultados = entityManager
                .createQuery("SELECT c FROM Cliente c WHERE c.telefono = :Telefono", Cliente.class)
                .setParameter("Telefono", Telefono)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }*/
}
