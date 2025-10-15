package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Producto;

public class ProductoDAO extends AbstractDAO<Producto> {

    private final EntityManager em;
    private static boolean contadorInicializado = false;

    public ProductoDAO(EntityManager em) {
        super(Producto.class);
        this.em = em;

        if (!contadorInicializado) {
            sincronizarContador();
            contadorInicializado = true;
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void crear(Producto producto) {
        save(producto);
        sincronizarContador();
    }

    private void sincronizarContador() {
        try {
            String ultimoId = em
                    .createQuery("SELECT p.idProducto FROM Producto p WHERE p.idProducto LIKE 'PR%' ORDER BY p.idProducto DESC", String.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (ultimoId != null && ultimoId.startsWith("PR")) {
                int numero = Integer.parseInt(ultimoId.substring(2)); // quitar "PR"
                Producto.setContador(numero + 1);
                System.out.println("Contador" + (numero + 1));
            }
        } catch (NoResultException e) {
            Producto.setContador(1000);
            System.out.println("ℹ No hay productos registrados");
        } catch (Exception e) {
            Producto.setContador(1000);
            System.err.println("Error al sincronizar: " + e.getMessage());
        }
    }

    public String generarNuevoIdProducto() {
        return Producto.generarNuevoId();
    }

    public boolean eliminarProducto(String idProducto) {
        EntityTransaction et = null;
        boolean eliminado = false;

        try {
            et = em.getTransaction();
            et.begin();

            Producto producto = em.find(Producto.class, idProducto);

            if (producto != null) {
                if (!em.contains(producto)) {
                    producto = em.merge(producto);
                }
                em.remove(producto);
                eliminado = true;
            }

            et.commit();
            return eliminado;

        } catch (Exception e) {
            if (et != null && et.isActive()) et.rollback();
            e.printStackTrace();
        }

        return eliminado;
    }

    public Producto buscarProductoPorId(String id) {
        try {
            return em.find(Producto.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el producto por ID", e);
        }
    }
}
