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

    // Variables globales
    private Double montoTotal = 0.0;
    private Double montoIngresado = 0.0;
    private Double montoFaltante = 0.0;
    private Double montoVenta = 0.0;
    private Double montoCambio = 0.0;
    private Double descuento = 0.0;
    private Date fecha;
    private Double monto;
    private byte porPagar;

    // Clases para completar el pago
    private Paga paga = new Paga();
    private Cliente cliente;
    private String idCliente;
    Membresia nueva = new Membresia();

    // Usuario recepcionista
    private String idUR;
    private String contrasenaUR;
    private Usuariorecepcionista usuarioRecepcionista;

    // Helpers necesarios
    private final PagaHelper pagaHelper = new PagaHelper();
    private final UsuarioRHelper usuarioHelper = new UsuarioRHelper();
    private final ClienteHelper clienteHelper = new ClienteHelper();
    private final AsignarClaseHelper AsignarClasehelper = new AsignarClaseHelper();
    private final ClaseHelper claseHelper = new ClaseHelper();
    private final MembresiaHelper membresiaHelper = new MembresiaHelper();

    // Esta funcion verifica si el ID del recepcionista es valido o existente
    public void verificarUsuario() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // Si el ID se deja vacio
            if (idUR == null || idUR.trim().isEmpty())
                throw new Exception("Debe ingresar el ID del usuario recepcionista.");

            // Si se ingreso algo en campo de ID en el xhtml entonces obtiene al usuario con su ID
            usuarioRecepcionista = usuarioHelper.obtenerUsuarioR(idUR.trim());
            // Si el usuario es null quiere decir que no se encontro un usuario con ese ID
            if (usuarioRecepcionista == null)
                throw new Exception("No se encontró un usuario con ese ID.");

            // Si se ecuentra un usuario entonces devuelve el mensaje Usuario verificado...
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario verificado", "Recepcionista encontrado."));
        } catch (Exception e) {
            // Si no se encontro entonces vuelve nula la instancia de usuarioRecepcionista y no preocede al modal de ingresar contraseña
            usuarioRecepcionista = null;
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al verificar usuario", e.getMessage()));
        }
    }

    // Esta funcion verifica la contraseña del usuarioRecepcionista anteriormente encontrado (Para mas seguridad)
    public void validarContrasena() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // Si el usuario es nulo quiere decir que primeramte no se ah encontrado el usuarioRecepcionista y que se debe enontrar para poder ingresar su contraseña
            if (usuarioRecepcionista == null)
                throw new Exception("Debe verificar primero al usuario recepcionista antes de validar la contraseña.");

            // Si la contraseña esta vacia entonces muestra el mensaje
            if (contrasenaUR == null || contrasenaUR.trim().isEmpty())
                throw new Exception("Debe ingresar la contraseña del recepcionista.");

            // Si contraseña no es agual a la contraseña que tiene el usuarioRecepcionita entonces muestra el mensaje
            if (!usuarioRecepcionista.getContrasena().equals(contrasenaUR)) {
                fc.validationFailed();
                throw new Exception("Contraseña incorrecta.");
            }

            // Si se identifica correctamente entoces muestra el siguiente mensaje
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso autorizado", "El recepcionista ha sido autenticado correctamente."));
        } catch (Exception e) {
            // Si no, entonces muestra el siguiente mensaje
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de autenticación", e.getMessage()));
        }
    }

    // Esta funcion calculta el faltante del dinero que ingreso el usuarioRecepcionista en el pago interactivo contra el monto total
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

    // Esta funcion realiza el pago interactivo de membresia
    public void realizarPagoInteractivoMembresia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // Esta funcion guarda en una variable String el tipo de pago que se va arealizar y ademas inicializa los montos totales
            String tipo = obtenerTotal("membresia");

            // Si el usuarioRcepcionista es nulo entonces muestro el siguiente mensaje
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            // Si el monto total el menor a 0 muestra el siguiente mensaje
            if (montoTotal < 0)
                throw new Exception("Monto total inválido. Seleccione un tipo de pago o calcule el total.");

            // Si el monto ingresado esta vacio menor que 0 muestra el siguiente mensaje
            if (montoIngresado == null || montoIngresado < 0)
                throw new Exception("Debe ingresar un monto para continuar.");

            // La funcion que realiza la logica de calcular el faltante del monto total contra lo que ingreso el usuarioRecepcionista(UR)
            calcularFaltante();

            // Si el montro ingresado es menor al monto total entonces muestra el siguiente mensaje
            if (montoIngresado < montoTotal) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Monto insuficiente", "Faltan " + montoFaltante + " pesos."));
                return; // Sale de la funcion
            }

            // Calcular el monto de cambio cuando sera 0.0
            montoCambio = montoIngresado - montoTotal;
            if (montoCambio < 0) montoCambio = 0.0;

            // Si el cliente es igual a null no sigue el flujo y muestra el siguiente mensaje
            if (cliente == null)
                throw new Exception("Debe seleccionar un cliente antes de realizar el pago.");

            // Si llego hasta aqui obtiene el cliente con su ID y lo asigna a un cliente auxiliar
            Cliente clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
            if (clienteExistente == null) {
                clienteHelper.AltaCliente(cliente);
            } else {
                cliente = clienteExistente;
            }

            // Si el tipo es membresia entonces realizo lo siguiente (Anteriormente estaba pensado de otra manera)
            if (tipo.equalsIgnoreCase("membresia")) {
                // Obtiene la membresia por el cliente para ver si este ya tiene una membresia
                Membresia membresiaActual = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente());

                // boleano para verificacion
                boolean tieneActiva = false;

                // Verifico si tiene membresia
                if (membresiaActual != null && membresiaActual.getFechaVencimiento() != null) {
                    LocalDate fechaV = membresiaActual.getFechaVencimiento();

                    // Si si la tiene entonces muestro el siguiente mensaje
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

                    // Creo membresia y desde la membresia el Item
                    nueva = new Membresia();
                    nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
                    nueva.setIdCliente(cliente);
                    nueva.setTipo("membresia");
                    membresiaHelper.registrarMembresia(nueva, cliente);

                    // Asigno el pago mensual a 800 si paga si paga su membresia
                    double gastoActual = cliente.getCantidadDineroMensual();
                    cliente.setCantidadDineroMensual(800.0);
                    clienteHelper.ModificarCliente(cliente);

                    // Creo el pago
                    paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                    paga.setFecha(LocalDate.now());
                    paga.setIdCliente(cliente);
                    paga.setMonto(montoTotal);
                    paga.setPorPagar(porPagar);

                    // Ralizo el pago
                    pagaHelper.RealizarPago(paga, tipo, nueva);

                } else {
                    return;
                }
            }

            // actualizo el cambio
            PrimeFaces.current().ajax().update("formPrincipal:dlgCambio");
            // Oculto el pago interactivo y muestro el cambio
            PrimeFaces.current().executeScript("PF('dlgPagoInteractivo').hide(); PF('dlgCambio').show();");

            // Reinicio las variables a 0.0
            montoIngresado = 0.0;
            montoFaltante = 0.0;

        } catch (Exception e) {
            // Si hay una excepcion, entonces muesro el siguiente mensaje
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al realizar el pago", e.getMessage()));
        }
    }

    // Esta funcion realiza el pago con tarjeta de una membresia
    public void realizarPagoTarjetaMembresia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null)
                throw new Exception("Debe validar un recepcionista antes de realizar el pago.");

            if (paga == null) paga = new Paga();

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

                // Verifico si tiene membresia
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

                    double gastoActual = cliente.getCantidadDineroMensual();
                    cliente.setCantidadDineroMensual(800.0);
                    clienteHelper.ModificarCliente(cliente);

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

            double gastoActual = cliente.getCantidadDineroMensual();
            cliente.setCantidadDineroMensual(gastoActual + 500.0);
            clienteHelper.ModificarCliente(cliente);

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

            double gastoActual = cliente.getCantidadDineroMensual();
            cliente.setCantidadDineroMensual(gastoActual + 500.0);
            clienteHelper.ModificarCliente(cliente);

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
            this.cliente = null;
            if (this.idCliente != null && !this.idCliente.trim().isEmpty()) {
                this.cliente = clienteHelper.obtenerCliente(this.idCliente.trim());
                if (this.cliente == null) {
                    throw new Exception("No se encontró ningún cliente con el ID: " + this.idCliente);
                }
            } else {
                this.cliente = (Cliente) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("clienteSeleccionado");
            }
            if (this.cliente == null) {
                throw new Exception("Debe seleccionar un cliente o ingresar un ID de cliente válido.");
            }
            obtenerTotal(tipo);
            this.fecha = new Date();
        } catch (Exception e) {
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al preparar pago", e.getMessage()));
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

        // Aplica descuento
        if (descuento != null && descuento > 0) {
            montoTotal -= descuento;
            if (montoTotal < 0) montoTotal = 0.0;
        }

        calcularFaltante();
        return tipo;
    }

    // Getters y Setters
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

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
