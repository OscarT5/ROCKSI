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

    public void eliminarCliente(String idCliente) {
        EntityTransaction et = null;

        try {
            et = entityManager.getTransaction();
            et.begin();//Se crea la transaccion de datos con la base de datos

            Cliente cliente = entityManager.find(Cliente.class, idCliente); //Busca el cliente dentro de la BD
            if (cliente != null) {
                entityManager.remove(cliente);//Prepara la eliminacion del cliente, la cual se hara proximamente
            }
            et.commit();//Aqui se realiza la accion de eliminacion, siempre y cuando si sea encontrado
        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
