package mx.desarollo.delegate;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.ClienteDAO;
import mx.avanti.desarollo.dao.PagaDAO;
import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Membresia;
import mx.desarollo.entity.Paga;
import mx.desarollo.entity.Producto;
import mx.desarollo.dto.ClienteNuevoDTO;
import mx.desarollo.dto.ProductoMensualDTO;
import mx.desarollo.dto.ReporteMensualDTO;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//Delegate que contiene toda la logica del reporte mensual
public class ReporteMensualDelegate {

    public ReporteMensualDelegate() {}

    //Recolectamos y procesamos toda la informacion del reporte mensual
    public ReporteMensualDTO generarDatosReporteMensual(LocalDate fechaActual) {

        //Determinar el rango de fechas del mes
        LocalDate inicioMes = fechaActual.withDayOfMonth(1);
        LocalDate finMes = fechaActual.with(TemporalAdjusters.lastDayOfMonth());

        //Convertir a DATE
        java.util.Date inicioMesDate = java.util.Date.from(inicioMes.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.util.Date finMesDate = java.util.Date.from(finMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            PagaDAO pagaDAO = new PagaDAO(em);
            ClienteDAO clienteDAO = new ClienteDAO(em);
            ProductoDAO productoDAO = new ProductoDAO(em);

            List<Paga> pagosDelMes = pagaDAO.findByFechaBetween(inicioMes, finMes);
            List<Cliente> clientesNuevos = clienteDAO.findByFechaRegistroBetween(inicioMesDate, finMesDate);
            List<Producto> todosLosProductos = productoDAO.findAll();

            //Procesar datos y convertirlos a los dtos con ayuda de las clases que se crearon
            List<ProductoMensualDTO> productosDTO = procesarProductosMensual(todosLosProductos, pagosDelMes);
            List<ClienteNuevoDTO> clientesDTO = procesarClientesNuevos(clientesNuevos, pagosDelMes);

            //Crear el tituto
            String mesFormateado = capitalizar(fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
            String mesYAnio = mesFormateado + " " + fechaActual.getYear();
            return new ReporteMensualDTO(mesYAnio, productosDTO, clientesDTO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar los datos del reporte mensual: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    //Calcular la cantidad vendida de los productos en el mes
    private List<ProductoMensualDTO> procesarProductosMensual(List<Producto> productos, List<Paga> pagos) {
        Map<String, Integer> mapaVentas = new HashMap<>();

        for (Paga pago : pagos) {
            if (pago.getIdItem() instanceof Producto) {
                Producto p = (Producto) pago.getIdItem();
                String idProducto = p.getIdItem();

                // Calcular cantidad (Monto / Precio)
                int cantidadVendida = 0;
                if (p.getPrecio() != null && p.getPrecio() > 0) {
                    cantidadVendida = (int) (pago.getMonto() / p.getPrecio());
                }

                // Acumular
                mapaVentas.put(idProducto, mapaVentas.getOrDefault(idProducto, 0) + cantidadVendida);
            }
        }

        List<ProductoMensualDTO> dtos = new ArrayList<>();
        for (Producto p : productos) {
            dtos.add(new ProductoMensualDTO(
                    p.getNombre(),
                    mapaVentas.getOrDefault(p.getIdItem(), 0), // Cantidad vendida
                    p.getStock() // Cantidad actual
            ));
        }
        return dtos;
    }

    //Construye la lista de clientes
    private List<ClienteNuevoDTO> procesarClientesNuevos(List<Cliente> clientesNuevos, List<Paga> pagos) {
        // Mapa de las membresías compradas este mes (ID_Cliente -> ID_Membresia)
        Map<String, String> mapaMembresias = new HashMap<>();
        for (Paga pago : pagos) {
            if (pago.getIdItem() instanceof Membresia) {
                mapaMembresias.put(pago.getIdCliente().getIdCliente(), pago.getIdItem().getIdItem());
            }
        }

        List<ClienteNuevoDTO> dtos = new ArrayList<>();
        for (Cliente c : clientesNuevos) {
            dtos.add(new ClienteNuevoDTO(
                    c.getIdCliente(),
                    c.getNombreCompleto(),
                    mapaMembresias.getOrDefault(c.getIdCliente(), "N/A") // "ME1001" o "N/A"
            ));
        }
        return dtos;
    }

    //Solo pone en mayusculas el texto
    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}