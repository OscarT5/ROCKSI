package mx.desarollo.dto;

//Dto que sacara los datos que se desplegaran en los clientes nuevos del reporte mensual
public class ClienteNuevoDTO {
    private String idCliente;
    private String nombreCompleto;
    private String membresia;

    public ClienteNuevoDTO(String idCliente, String nombreCompleto, String membresia) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.membresia = membresia;
    }

    public String getIdCliente() { return idCliente; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getMembresia() { return membresia; }
}