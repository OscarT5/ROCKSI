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
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            Cliente.setContador(max + 1);
        } catch (Exception e) {
            // Si falla se deja el contador por defecto
        }
    }

    //Metodo para eliminar el cliente
    public boolean eliminarCliente(String idCliente) {
        EntityTransaction et = null;
        boolean eliminado = false;

        try {
            et = entityManager.getTransaction();//Aqui se abre la transaccion necesaria hacia la BD
            et.begin();

            Cliente cliente = entityManager.find(Cliente.class, idCliente);//Encuentra el id del cliente

            if (cliente != null) {
                if (!entityManager.contains(cliente)) {
                    cliente = entityManager.merge(cliente);
                }
                entityManager.remove(cliente);
                eliminado = true;
            }

            et.commit();//Realiza los cambios
            return eliminado;
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        }

        return eliminado;
    }

    public Cliente buscarPorId(int id) {
        Optional<Cliente> opt = find(id);
        return opt.orElse(null);
    }

    public Cliente buscarPorId(String id) {
        try {
            return entityManager.find(Cliente.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente por ID", e);
        }
    }

    public List<Cliente> listarTodos() {
        return findAll();
    }

    public void eliminar(int id) {
        Optional<Cliente> opt = find(id);
        opt.ifPresent(this::delete);
    }

    public void actualizar(Cliente cliente) {
        EntityTransaction tx = null;

        try {
            tx = entityManager.getTransaction();

            // Iniciar transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualizar el cliente existente
            entityManager.merge(cliente);

            // Confirmar los cambios
            tx.commit();

        } catch (Exception e) {
            // Revertir la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar el cliente.", e);
        }
    }
    public Cliente buscarPorTelefono(String Telefono) {
        List<Cliente> resultados = entityManager
                .createQuery("SELECT c FROM Cliente c WHERE c.telefono = :Telefono", Cliente.class)
                .setParameter("Telefono", Telefono)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
