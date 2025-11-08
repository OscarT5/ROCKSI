package mx.desarollo.delegate;

import jakarta.persistence.EntityManager;
import mx.avanti.desarollo.dao.InventarioDiarioDAO;
import mx.avanti.desarollo.dao.PagaDAO;
import mx.avanti.desarollo.dao.ProductoDAO;
import mx.avanti.desarollo.persistence.HibernateUtil;
import mx.desarollo.entity.*;
import mx.desarollo.dto.ProductoReporteDTO;
import mx.desarollo.dto.PagoReporteDTO;
import mx.desarollo.dto.ReporteDiarioDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteDelegate {

    public ReporteDelegate() {}

    //Metodo principal que recolecta, procesa y empaqueta toda la información necesaria para el Reporte Diario.
    public ReporteDiarioDTO generarDatosReporteDiario(LocalDate fecha) {

        EntityManager em = HibernateUtil.getEntityManager();

        try {
            //Instanciar todos los daos que necesitamos
            PagaDAO pagaDAO = new PagaDAO(em);
            ProductoDAO productoDAO = new ProductoDAO(em);
            InventarioDiarioDAO inventarioDAO = new InventarioDiarioDAO(em);

            //Procesar los pagos
            List<Paga> pagosDelDia = pagaDAO.findByFecha(fecha);
            List<PagoReporteDTO> pagosDTO = procesarPagos(pagosDelDia);

            //Procesar los productos
            List<Producto> todosLosProductos = productoDAO.findAll();
            List<InventarioDiario> snapshots = inventarioDAO.findByFecha(fecha);
            List<ProductoReporteDTO> productosDTO = procesarProductos(todosLosProductos, snapshots, pagosDelDia);
            //(Pendiente) Procesar Retiros de Caja
            //(Pendiente) Procesar Cierre de Caja

            return new ReporteDiarioDTO(fecha, productosDTO, pagosDTO);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar los datos del reporte: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    //Convierte la lista de entidades de Paga a una lista de dtos para el reporte
    private List<PagoReporteDTO> procesarPagos(List<Paga> pagos) {
        List<PagoReporteDTO> dtos = new ArrayList<>();

        for (Paga p : pagos) {
            String idCliente = p.getIdCliente().getIdCliente();
            String nombreCliente = p.getIdCliente().getNombreCompleto(); // Sigo asumiendo que Cliente.java tiene getNombreCompleto()
            String idPago = p.getIdPaga();
            double total = p.getMonto();

            //Variable para guardar el nombre del artículo
            String articulo = "";
            Item item = p.getIdItem();

            //Aqui se comprueba de que tipo es el item
            if (item instanceof Producto) {
                //Si es Producto lo convertimos a Producto y usamos getNombre()
                articulo = ((Producto) item).getNombre();

            } else if (item instanceof Clase) {
                //Si es Clase lo convertimos a Clase y usamos getNombre()
                articulo = ((Clase) item).getNombre();

            } else if (item instanceof Membresia) {
                // Membresia no tiene nombre asi que le asignamos uno
                articulo = "Membresía";

            }
            dtos.add(new PagoReporteDTO(idCliente, nombreCliente, idPago, articulo, total));
        }
        return dtos;
    }

    private List<ProductoReporteDTO> procesarProductos(List<Producto> productos, List<InventarioDiario> snapshots, List<Paga> pagos) {

        Map<String, Integer> mapaStockInicial = new HashMap<>();
        for (InventarioDiario snapshot : snapshots) {
            mapaStockInicial.put(snapshot.getIdProducto(), snapshot.getStockInicial());
        }

        Map<String, Integer> mapaVentas = new HashMap<>();
        for (Paga pago : pagos) {
            // Solo procesamos pagos que son de Productos
            if (pago.getIdItem() instanceof Producto) {
                Producto p = (Producto) pago.getIdItem();
                String idProducto = p.getIdItem();
                //Aqui se calculan los articulos vendidos dividiendo el precio final por el precio del articulo
                int cantidadVendida = 0;
                if (p.getPrecio() != null && p.getPrecio() > 0) {
                    cantidadVendida = (int) (pago.getMonto() / p.getPrecio());
                }
                //Acumulamos por si se vendio el mismo producto en pagos diferentes
                mapaVentas.put(idProducto, mapaVentas.getOrDefault(idProducto, 0) + cantidadVendida);
            }
        }

        List<ProductoReporteDTO> dtos = new ArrayList<>();
        for (Producto p : productos) {
            String id = p.getIdItem();
            int inicial = mapaStockInicial.getOrDefault(id, 0);
            int vendida = mapaVentas.getOrDefault(id, 0);
            int fin = p.getStock(); //Stock final
            dtos.add(new ProductoReporteDTO(p.getNombre(), inicial, vendida, fin));
        }
        return dtos;
    }
}