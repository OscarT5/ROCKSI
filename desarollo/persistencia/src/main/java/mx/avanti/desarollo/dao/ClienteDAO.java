package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import mx.desarollo.entity.Cliente;

public class ClienteDAO  {


    private EntityManagerFactory emf; // fabrica de entity manager si se usa localmente
    private EntityManager externalEm; // entity manager proporcionado por fuera

    public ClienteDAO() { // constructor por defecto que crea el emf
        this.emf = Persistence.createEntityManagerFactory("ROCKSIPU"); // crea la fabrica con la unidad de persistencia
    }

    public ClienteDAO(EntityManager em) { // constructor que recibe un entity manager externo
        this.externalEm = em; // guarda el entity manager recibido
    }

    private EntityManager createEntityManager() { // metodo que decide que entity manager usar
        return (externalEm != null) ? externalEm : emf.createEntityManager(); // usa el externo si existe, si no crea uno nuevo
    }

    // metodo para dar de alta un cliente
    public void crear(Cliente cliente) {
        EntityManager em = createEntityManager(); // obtiene el entity manager adecuado
        boolean mustClose = (externalEm == null); // si no hay external em, este metodo debe cerrar el em
        try {
            if (!em.getTransaction().isActive()) em.getTransaction().begin(); // inicia la transaccion si no esta activa
            em.persist(cliente); // persiste la entidad cliente
            em.getTransaction().commit(); // confirma la transaccion
        } catch (Exception e) { // en caso de error
            if (em.getTransaction().isActive()) em.getTransaction().rollback(); // revierte la transaccion si esta activa
            throw e; // relanza la excepcion hacia arriba
        } finally {
            if (mustClose) em.close(); // cierra el entity manager si fue creado aqui
        }
    }

    /*public Cliente buscarPorId(int id) {
        EntityManager em = createEntityManager();
        boolean mustClose = (externalEm == null);
        try {
            return em.find(Cliente.class, id);
        } finally {
            if (mustClose) em.close();
        }
    }

    public List<Cliente> listarTodos() {
        EntityManager em = createEntityManager();
        boolean mustClose = (externalEm == null);
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            if (mustClose) em.close();
        }
    }

    public void eliminar(int id) {
        EntityManager em = createEntityManager();
        boolean mustClose = (externalEm == null);
        try {
            if (!em.getTransaction().isActive()) em.getTransaction().begin();
            Cliente c = em.find(Cliente.class, id);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            if (mustClose) em.close();
        }
    }

    public void actualizar(Cliente cliente) {
        EntityManager em = createEntityManager();
        boolean mustClose = (externalEm == null);
        try {
            if (!em.getTransaction().isActive()) em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            if (mustClose) em.close();
        }
    }*/
}