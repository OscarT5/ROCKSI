package ui;

import helper.*;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.*;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Named("RealizarPagoBeanUI")
@SessionScoped
public class RealizarPagoBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;
    Membresia nueva = new Membresia();

    private Double montoTotal = 0.0;
    private Double montoIngresado = 0.0;
    private Double montoFaltante = 0.0;
    private Double montoVenta = 0.0;
    private Double montoCambio = 0.0;
    private Double descuento = 0.0;

    private Paga paga = new Paga();
    private Cliente cliente;
    private Date fecha;
    private Double monto;
    private byte porPagar;

    private String idUR;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioRecepcionista;

    private final PagaHelper pagaHelper = new PagaHelper();
    private final UsuarioRHelper usuarioHelper = new UsuarioRHelper();
    private final ClienteHelper clienteHelper = new ClienteHelper();
    private final AsignarClaseHelper AsignarClasehelper = new AsignarClaseHelper();
    private final ClaseHelper claseHelper = new ClaseHelper();
    private final MembresiaHelper membresiaHelper = new MembresiaHelper();

    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (idUR == null || idUR.trim().isEmpty())
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");

            usuarioRecepcionista = usuarioHelper.obtenerUsuarioR(idUR.trim());
            if (usuarioRecepcionista == null)
                throw new Exception("No se encontró un usuario con ese ID.");

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario verificado", "Recepcionista encontrado."));
        } catch (Exception e) {
            usuarioRecepcionista = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar usuario", e.getMessage()));
        }
    }

    public void validarContrasena() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe verificar primero al usuario recepcionista antes de validar la contraseña.");

            if (contrasenaUR == null || contrasenaUR.trim().isEmpty())
                throw new Exception("Debe ingresar la contraseña del recepcionista.");

            if (!usuarioRecepcionista.getContrasena().equals(contrasenaUR)) {
                fc.validationFailed();
                throw new Exception("Contraseña incorrecta.");
            }

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso autorizado", "El recepcionista ha sido autenticado correctamente."));
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", e.getMessage()));
        }
    }

    private void calcularFaltante() {
        if (montoIngresado == null) montoIngresado = 0.0;
        if (montoTotal == null) montoTotal = 0.0;

        if (montoIngresado < montoTotal) {
            montoFaltante = montoTotal - montoIngresado;
            montoCambio = 0.0;
        } else {
            montoFaltante = 0.0;
            montoCambio = montoIngresado - montoTotal;
        }
    }

    public void realizarPagoInteractivoMembresia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            String tipo = obtenerTotal("membresia");

            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            if (montoTotal < 0)
                throw new Exception("Monto total inválido. Seleccione un tipo de pago o calcule el total.");

            if (montoIngresado == null || montoIngresado < 0)
                throw new Exception("Debe ingresar un monto para continuar.");

            calcularFaltante();

            if (montoIngresado < montoTotal) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Monto insuficiente", "Faltan " + montoFaltante + " pesos."));
                return;
            }

            montoCambio = montoIngresado - montoTotal;
            if (montoCambio < 0) montoCambio = 0.0;

            // Obtener cliente desde la sesión si no está asignado
            if (cliente == null) {
                cliente = (Cliente) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("clienteSeleccionado");
            }

            if (cliente == null)
                throw new Exception("Debe seleccionar un cliente antes de realizar el pago.");

            Cliente clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
            if (clienteExistente == null) {
                clienteHelper.AltaCliente(cliente);
            } else {
                cliente = clienteExistente;
            }


            if (tipo.equalsIgnoreCase("membresia")) {
                Membresia membresiaActual = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente());

                boolean tieneActiva = false;

                // Verificar si ya tiene una membresia
                if (membresiaActual != null && membresiaActual.getFechaVencimiento() != null) {
                    LocalDate fechaV = membresiaActual.getFechaVencimiento();

                    if (fechaV.isAfter(LocalDate.now())) {
                        tieneActiva = true;
                        fc.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_WARN,
                                "Membresía activa",
                                "El cliente ya tiene una membresía vigente hasta " + fechaV + "."));
                    }
                }

                // Si no tiene membresia crea una nueva
                if (!tieneActiva) {
                    clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
                    if (clienteExistente == null) {
                        clienteHelper.AltaCliente(cliente);
                    } else {
                        cliente = clienteExistente;
                    }

                    nueva = new Membresia();
                    nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
                    nueva.setIdCliente(cliente);
                    nueva.setTipo("membresia");
                    membresiaHelper.registrarMembresia(nueva, cliente);

                    paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                    paga.setFecha(LocalDate.now());
                    paga.setIdCliente(cliente);
                    paga.setMonto(montoTotal);
                    paga.setPorPagar(porPagar);

                    pagaHelper.RealizarPago(paga, tipo, nueva);

                } else {
                    return;
                }
            }

            PrimeFaces.current().ajax().update("formPrincipal:dlgCambio");
            PrimeFaces.current().executeScript("PF('dlgPagoInteractivo').hide(); PF('dlgCambio').show();");

            montoIngresado = 0.0;
            montoFaltante = 0.0;

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al realizar el pago", e.getMessage()));
        }
    }

    public void realizarPagoTarjetaMembresia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            if (paga == null) paga = new Paga();

            if (cliente == null) {
                cliente = (Cliente) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("clienteSeleccionado");
            }

            if (cliente == null)
                throw new Exception("Debe seleccionar un cliente antes de realizar el pago.");

            Cliente clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());

            if (clienteExistente == null) {
                clienteHelper.AltaCliente(cliente);
            } else {
                cliente = clienteExistente;
            }

            String tipo = obtenerTotal("membresia");

            if (cliente == null)
                throw new Exception("Debe seleccionar un cliente antes de realizar el pago.");

            clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
            if (clienteExistente == null) {
                clienteHelper.AltaCliente(cliente);
            } else {
                cliente = clienteExistente;
            }


            if (tipo.equalsIgnoreCase("membresia")) {
                Membresia membresiaActual = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente());

                boolean tieneActiva = false;

                // Verificar si ya tiene una membresia
                if (membresiaActual != null && membresiaActual.getFechaVencimiento() != null) {
                    LocalDate fechaV = membresiaActual.getFechaVencimiento();

                    if (fechaV.isAfter(LocalDate.now())) {
                        tieneActiva = true;
                        fc.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_WARN,
                                "Membresía activa",
                                "El cliente ya tiene una membresía vigente hasta " + fechaV + "."));
                    }
                }

                // Si no tiene membresia crea una nueva
                if (!tieneActiva) {
                    clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
                    if (clienteExistente == null) {
                        clienteHelper.AltaCliente(cliente);
                    } else {
                        cliente = clienteExistente;
                    }

                    nueva = new Membresia();
                    nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
                    nueva.setIdCliente(cliente);
                    nueva.setTipo("membresia");
                    membresiaHelper.registrarMembresia(nueva, cliente);

                    paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                    paga.setFecha(LocalDate.now());
                    paga.setIdCliente(cliente);
                    paga.setMonto(montoTotal);
                    paga.setPorPagar(porPagar);

                    pagaHelper.RealizarPago(paga, tipo, nueva);

                } else {
                    return;
                }
            }

            PrimeFaces.current().executeScript("PF('dlgPagoTarjeta').hide(); PF('dlgExitoTarjeta').show();");

            limpiarCampos();
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al realizar el pago con tarjeta", e.getMessage()));
        }
    }

    public void realizarPagoInteractivoClase() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            String idClase = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idClase");
            String idCliente = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idCliente");

            if (idClase == null || idClase.trim().isEmpty())
                throw new Exception("No se ha seleccionado ninguna clase.");

            if (idCliente == null || idCliente.trim().isEmpty())
                throw new Exception("No se ha seleccionado ningún cliente.");

            Cliente clienteExistente = clienteHelper.obtenerCliente(idCliente);
            if (clienteExistente == null)
                throw new Exception("No se encontró el cliente con ID: " + idCliente);

            this.cliente = clienteExistente;

            Clase clase = claseHelper.obtenerClase(idClase);
            if (clase == null)
                throw new Exception("No se encontró la clase con ID: " + idClase);

            boolean yaAsignado = AsignarClasehelper.verificarClaseAsignadaACliente(idCliente, idClase);
            if (yaAsignado) {
                fc.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Clase ya asignada",
                        "El cliente ya está inscrito en la clase " + clase.getNombre() + "."));
                return;
            }

            int inscritos = clase.getClientes().size();
            if (inscritos >= clase.getCupoMaximo())
                throw new Exception("La clase " + clase.getNombre() + " ha alcanzado su cupo máximo de " + clase.getCupoMaximo() + " participantes.");

            AsignarClasehelper.asignarClaseACliente(idCliente, idClase);

            nueva = new Membresia();
            nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
            nueva.setTipo("clase");
            nueva.setIdCliente(cliente);
            membresiaHelper.registrarMembresia(nueva, cliente);

            paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
            paga.setFecha(LocalDate.now());
            paga.setIdCliente(cliente);
            paga.setMonto(montoTotal);
            paga.setPorPagar(porPagar);

            pagaHelper.RealizarPago(paga,"clase", nueva);

            PrimeFaces.current().executeScript("PF('dlgPagoInteractivo').hide(); PF('dlgCambio').show();");

            limpiarCampos();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idClase");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idCliente");

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al realizar pago de clase", e.getMessage()));
        }
    }

    public void realizarPagoTarjetaClase() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            String idClase = (String) fc.getExternalContext().getSessionMap().get("idClase");
            String idCliente = (String) fc.getExternalContext().getSessionMap().get("idCliente");

            if (idClase == null || idClase.trim().isEmpty())
                throw new Exception("No se ha seleccionado ninguna clase.");

            if (idCliente == null || idCliente.trim().isEmpty())
                throw new Exception("No se ha seleccionado ningún cliente.");

            Cliente clienteExistente = clienteHelper.obtenerCliente(idCliente);
            if (clienteExistente == null)
                throw new Exception("No se encontró el cliente con ID: " + idCliente);

            this.cliente = clienteExistente;

            Clase clase = claseHelper.obtenerClase(idClase);
            if (clase == null)
                throw new Exception("No se encontró la clase con ID: " + idClase);

            boolean yaAsignado = AsignarClasehelper.verificarClaseAsignadaACliente(idCliente, idClase);
            if (yaAsignado) {
                fc.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Clase ya asignada",
                        "El cliente ya está inscrito en la clase " + clase.getNombre() + "."));
                return;
            }

            int inscritos = clase.getClientes().size();
            if (inscritos >= clase.getCupoMaximo())
                throw new Exception("La clase " + clase.getNombre() + " ha alcanzado su cupo máximo de " + clase.getCupoMaximo() + " participantes.");

            AsignarClasehelper.asignarClaseACliente(idCliente, idClase);

            nueva = new Membresia();
            nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
            nueva.setTipo("clase");
            nueva.setIdCliente(cliente);
            membresiaHelper.registrarMembresia(nueva, cliente);

            paga = new Paga();
            paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
            paga.setFecha(LocalDate.now());
            paga.setIdCliente(cliente);
            paga.setMonto(montoTotal > 0 ? montoTotal : 500.00);
            paga.setPorPagar(porPagar);

            pagaHelper.RealizarPago(paga, "clase", nueva);

            PrimeFaces.current().executeScript("PF('dlgPagoTarjeta').hide(); PF('dlgExitoTarjeta').show();");

            limpiarCampos();
            fc.getExternalContext().getSessionMap().remove("idClase");
            fc.getExternalContext().getSessionMap().remove("idCliente");

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error al realizar pago con tarjeta",
                    e.getMessage()
            ));
        }
    }

    public void prepararPago(String tipo) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (cliente == null) {
                cliente = (Cliente) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("clienteSeleccionado");
            }

            obtenerTotal(tipo);

            this.fecha = new Date();

        } catch (Exception e) {
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al preparar pago", "No se pudieron cargar los datos: " + e.getMessage()));
        }
    }

    public void cancelarPago() {
        try {
            limpiarCampos();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago cancelado", "El proceso de pago ha sido cancelado."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al cancelar pago", e.getMessage()));
        }
    }

    public void limpiarCampos() {
        cliente = null;
        monto = null;
        fecha = null;
        porPagar = 0;
        contrasenaUR = null;
        idUR = null;
        usuarioRecepcionista = null;
        paga = new Paga();
        montoTotal = 0.0;
        montoIngresado = 0.0;
        montoFaltante = 0.0;
        descuento = 0.0;
    }

    private String obtenerTotal(String tipo) {
        if (tipo == null) tipo = "";
        tipo = tipo.toLowerCase();

        switch (tipo) {
            case "membresia":
                montoTotal = 800.00;
                if (descuento != null && descuento > 800.00) {
                    descuento = 800.00;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Descuento ajustado",
                                    "El descuento no puede ser mayor a $800."));
                }
                break;

            case "clase":
                montoTotal = 500.00;
                if (descuento != null && descuento > 500.00) {
                    descuento = 500.00;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Descuento ajustado",
                                    "El descuento no puede ser mayor a $500."));
                }
                break;

            case "venta":
                montoTotal = (montoVenta != null && montoVenta > 0) ? montoVenta : 0.0;
                if (descuento != null && descuento > montoTotal) {
                    descuento = montoTotal;
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Descuento ajustado",
                                    "El descuento no puede ser mayor al monto de la venta."));
                }
                break;

            default:
                montoTotal = 0.0;
                break;
        }

        // Aplica descuento (ya validado)
        if (descuento != null && descuento > 0) {
            montoTotal -= descuento;
            if (montoTotal < 0) montoTotal = 0.0;
        }

        calcularFaltante();
        return tipo;
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public byte getPorPagar() { return porPagar; }
    public void setPorPagar(byte porPagar) { this.porPagar = porPagar; }

    public String getIdUR() { return idUR; }
    public void setIdUR(String idUR) { this.idUR = idUR; }

    public String getContrasenaUR() { return contrasenaUR; }
    public void setContrasenaUR(String contrasenaUR) { this.contrasenaUR = contrasenaUR; }

    public Usuariorecepcionista getUsuarioRecepcionista() { return usuarioRecepcionista; }

    public Double getMontoTotal() { return montoTotal; }
    public Double getMontoIngresado() { return montoIngresado; }

    public void setMontoIngresado(Double montoIngresado) {
        this.montoIngresado = montoIngresado;
        calcularFaltante();
    }

    public Double getMontoCambio() { return montoCambio; }
    public void setMontoCambio(Double montoCambio) { this.montoCambio = montoCambio; }

    public Double getMontoFaltante() { return montoFaltante; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }
}
