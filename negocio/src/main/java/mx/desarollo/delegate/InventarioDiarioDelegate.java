package mx.desarollo.delegate;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.InventarioDiarioDAO;
import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.InventarioDiario;
import mx.desarollo.entity.Producto;

import java.time.LocalDate;
import java.util.List;

public class InventarioDiarioDelegate {

    public InventarioDiarioDelegate() {}

    public void crearSnapshotDiario() throws SnapshotException {
        LocalDate hoy = LocalDate.now();

        //Crea un EM para toda la operacion
        EntityManager em = HibernateUtil.getEntityManager();

        //Pasa el EM a los DAOs correspondientes
        InventarioDiarioDAO inventarioDAO = new InventarioDiarioDAO(em);
        ProductoDAO productoDAO = new ProductoDAO(em);

        try {
            //Verifica si el boton ya se presiono hoy,es decir, que el proceso ya se hizo
            if (inventarioDAO.existeSnapshotParaFecha(hoy)) {
                throw new SnapshotException("El snapshot de inventario para el dia de hoy ya fue generado.");
            }

            //Obtiene productos existentes
            List<Producto> todosLosProductos = productoDAO.listarActivos();

            if (todosLosProductos == null || todosLosProductos.isEmpty()) {
                throw new SnapshotException("No se encontraron productos para generar el snapshot.");
            }

            //Guarda el snapshot de los productos
            for (Producto producto : todosLosProductos) {
                InventarioDiario snapshot = new InventarioDiario(
                        hoy,
                        producto.getIdItem(),
                        producto.getStock()
                );

                inventarioDAO.save(snapshot);
            }

        } catch (Exception e) {
            throw new SnapshotException("Error crítico al crear el snapshot: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public static class SnapshotException extends Exception {
        public SnapshotException(String message) {
            super(message);
        }

        public SnapshotException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}