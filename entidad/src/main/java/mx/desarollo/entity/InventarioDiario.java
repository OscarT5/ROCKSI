package mx.desarollo.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

//Esta entidad mapea a la tabla de inventario_diario que fue creada en la BD
@Entity
@Table(name = "inventario_diario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fecha", "id_producto"}, name = "uk_producto_dia")
)
public class InventarioDiario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "id_producto", nullable = false, length = 45)
    private String idProducto;

    @Column(name = "stock_inicial", nullable = false)
    private Integer stockInicial;


    public InventarioDiario() {
    }

    public InventarioDiario(LocalDate fecha, String idProducto, Integer stockInicial) {
        this.fecha = fecha;
        this.idProducto = idProducto;
        this.stockInicial = stockInicial;
    }

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getStockInicial() {
        return stockInicial;
    }

    public void setStockInicial(Integer stockInicial) {
        this.stockInicial = stockInicial;
    }
}