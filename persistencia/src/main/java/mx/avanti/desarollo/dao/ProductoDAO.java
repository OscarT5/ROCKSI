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
                    .createQuery("SELECT p.idItem FROM Producto p WHERE p.idItem LIKE 'PR%' ORDER BY p.idItem DESC", String.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (ultimoId != null && ultimoId.startsWith("PR")) {
                int numero = Integer.parseInt(ultimoId.substring(2)); // quitar "PR"
                Producto.setContador(numero + 1);
            }
        } catch (NoResultException e) {
            Producto.setContador(1000);
        } catch (Exception e) {
            Producto.setContador(1000);
            System.err.println("Error al sincronizar la id: " + e.getMessage());
        }
    }

    public String generarNuevoIdProducto() {
        return Producto.generarNuevoId();
    }
}
