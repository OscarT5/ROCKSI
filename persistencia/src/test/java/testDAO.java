import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.Producto;

public class testDAO {

    /**
     * Metodo main ejecutable para comprobar que mi capa de negocio se conecta y trae objetos de la base de datos
     * @return imprime en consola el Cliente + id[ID del cliente]
     */
    public static void main(String[] args) {
        ProductoDAO productoDAO = new ProductoDAO(HibernateUtil.getEntityManager());



        for (Producto pro : productoDAO.listarTodosLosProductos()) {
            System.out.println(pro + "|| id [" + pro.getIdProducto()+ "]");
        }
    }
}
