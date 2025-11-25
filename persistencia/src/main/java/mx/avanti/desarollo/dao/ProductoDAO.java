package mx.avanti.desarollo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import mx.avanti.desarollo.persistence.AbstractDAO;
import mx.desarollo.entity.Producto;

import java.util.List;

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

    //Se agrego el metodo de crear y no se uso save, puesto que al usar save, hibernate lanzaba error aun cuando todo estaba bien
    public void crear(Producto producto) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            if (producto.getTipo() == null) {
                producto.setTipo("producto");
            }
            if (producto.getIdUsuarioAdmin() == null) {
                producto.setIdUsuarioAdmin("ADM1000");
            }

            producto.setStatus((byte) 1);

            em.persist(producto);
            tx.commit();

            sincronizarContador();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al registrar el producto", e);
        }
    }


    /*
    En esta funcion se inicializa el contador para su respectivo ID que empieza con CLI
     */
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

    //Aqui se genera el nuevo ID
    public String generarNuevoIdProducto() {
        return Producto.generarNuevoId();
    }

    /**
     * Metodo para eliminar un producto por su ID
     * @Throws Si la base de datos rechaza la peticion, o no se encuentra el producto con el ID
     * @Params Un String id del producto
     * @return Una respuesta de tipo boolean
     */
    public boolean eliminarProducto(String idProducto) {
        EntityTransaction tx = null;
        boolean eliminado = false;

        try {
            // Primero buscamos el producto
            Producto producto = em.find(Producto.class, idProducto);

            // Si el producto es nulo (no existe) retornamos false
            if (producto == null) {
                return false;
            }

            if (producto.getStatus() == (byte) 0) {
                return false;
            }

            tx = em.getTransaction();

            if (!tx.isActive()) {
                tx.begin();
            }

            // Baja logica esto quiere decir cambiar el estatus a 0
            producto.setStatus((byte) 0);

            em.merge(producto);

            tx.commit();

            // Si todo salio bien entonces
            eliminado = true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el producto (Baja Lógica).", e);
        }
        return eliminado;
    }

    /**
     * Metodo para buscar un producto por su ID
     * @Throws Si la base de datos rechaza la peticion, o no se encuentra el producto con el ID
     * @Params Un String id del producto
     * @return Un objeto de tipo Producto
     */
    public Producto buscarProductoPorId(String id) {
        try {
            // Busca el producto por el id y si lo encuentra lo guarda en un objeto de tipo Producto
            Producto producto = em.find(Producto.class, id);
            // Si el status del producto es 0 entonces
            if (producto.getStatus() == (byte) 0) {
                return null; // retorna null
            }
            // Si no entonces retorna el producto
            return producto;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el producto por ID...", e);
        }
    }

    public void reducirStock(String idProducto) {
        EntityTransaction tx = em.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }

            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new RuntimeException("No se encontró el producto con ID: " + idProducto);
            }

            if (producto.getStock() <= 0) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no tiene stock disponible.");
            }

            producto.setStock(producto.getStock() - 1);
            em.merge(producto);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al reducir el stock del producto " + idProducto, e);
        }
    }

    public List<Producto> listarActivos() {
        return execute(em -> {
            em.clear();
            return em.createQuery("SELECT p FROM Producto p WHERE p.status = 1", Producto.class)
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        });
    }

    /**
     * Metodo para listar todos los productos de la base de datos
     * @Throws Si la base de datos rechaza la peticion
     * @return Una lista de productos
     */
    public List<Producto> listarTodosLosProductos() {
        return execute(em -> {
            em.clear();

            return em.createQuery(
                            "SELECT p FROM Producto p WHERE p.status = :status", Producto.class)
                    .setParameter("status", (byte) 1)
                    .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                    .getResultList();
        });
    }

    /**
     * Metodo para modificar los datos de un producto
     * @Throws Si la base de datos rechaza la peticion o el producto es null
     * @Params Un objeto de tipo Producto
     * @return void
     */
    public void actualizarProducto(Producto producto) {
        EntityTransaction tx = null;
        try {
            if (producto.getStatus().equals((byte) 0)) {
                throw new RuntimeException("Este producto ya a sido eliminado.");
            }

            tx = em.getTransaction();

            // Inicia la transaccion si no está activa
            if (!tx.isActive()) {
                tx.begin();
            }

            // Actualiza el producto existente
            em.merge(producto);

            // Confirma los cambios
            tx.commit();

        } catch (Exception e) {
            // Revierte la transaccion si ocurre un error
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al modificar el producto.", e);
        }
    }
}
