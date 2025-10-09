import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.Cliente;

import java.util.List;

public class testDAO {

    /**
     * Metodo main ejecutable para comprobar que mi capa de negocio se conecta y trae objetos de la base de datos
     * @return imprime en consola el Cliente + id[ID del cliente]
     */
    public static void main(String[] args) {
        ClienteDAO ClienteDAO = new ClienteDAO(HibernateUtil.getEntityManager());

        for (Cliente Cliente : ClienteDAO.listarTodos()) {
            System.out.println(Cliente + "|| id [" + Cliente.getIdCliente()+ "]");
        }
    }
}
