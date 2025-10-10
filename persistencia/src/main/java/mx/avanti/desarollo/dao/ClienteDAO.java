package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Cliente;

public class ClienteDAO extends AbstractDAO<Cliente> {

    private final EntityManager entityManager;

    public ClienteDAO(EntityManager em) {
        super(Cliente.class);
        this.entityManager = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public void crear(Cliente cliente) {
        save(cliente);
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
}
