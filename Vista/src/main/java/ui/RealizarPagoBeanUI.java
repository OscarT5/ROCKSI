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
    private String siguienteDialogo;
    private boolean clienteTieneCredito = false;
    private double creditoAplicado = 0.0;

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

            // Obtiene la membresia por el cliente para ver si este ya tiene una membresia
            Membresia membresiaActual = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente(),"membresia");

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
                pagaHelper.RealizarPago(paga, nueva.getIdItem());

            } else {
                return;
            }

            // actualizo el cambio
            PrimeFaces.current().ajax().update("formPrincipal:dlgCambio1");
            // Oculto el pago interactivo y muestro el cambio
            PrimeFaces.current().executeScript("PF('dlgPagoInteractivo1').hide(); PF('dlgCambio1').show();");

            // Reinicio las variables a 0.0
            montoIngresado = 0.0;
            montoFaltante = 0.0;
            fc.getExternalContext().getSessionMap().remove("clienteSeleccionado");
            limpiarCampos();

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

            if (cliente == null)
                throw new Exception("Debe seleccionar un cliente antes de realizar el pago.");

            clienteExistente = clienteHelper.obtenerCliente(cliente.getIdCliente());
            if (clienteExistente == null) {
                clienteHelper.AltaCliente(cliente);
            } else {
                cliente = clienteExistente;
            }

            Membresia membresiaActual = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente(),"membresia");

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

                pagaHelper.RealizarPago(paga, nueva.getIdItem());

                fc.getExternalContext().getSessionMap().remove("clienteSeleccionado");
                limpiarCampos();

            } else {
                return;
            }

            PrimeFaces.current().executeScript("PF('dlgPagoTarjeta1').hide(); PF('dlgExitoTarjeta1').show();");
            fc.getExternalContext().getSessionMap().remove("clienteSeleccionado");
            limpiarCampos();

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al realizar el pago con tarjeta", e.getMessage()));
        }
    }

    public void realizarPagoInteractivoClase() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null) throw new Exception("Debe validar un recepcionista.");
            if (montoTotal < 0) throw new Exception("Monto total inválido.");

            String idClase = (String) fc.getExternalContext().getSessionMap().get("idClase");
            String idClienteSesion = (String) fc.getExternalContext().getSessionMap().get("idCliente");
            if (idClase == null || idClienteSesion == null) throw new Exception("No se ha seleccionado una clase o cliente.");

            this.cliente = clienteHelper.obtenerCliente(idClienteSesion);
            if (cliente == null) throw new Exception("No se encontró el cliente con ID: " + idClienteSesion);

            Clase clase = claseHelper.obtenerClase(idClase);
            if (clase == null) throw new Exception("No se encontró la clase con ID: " + idClase);

            if (clase.getClientes().size() >= clase.getCupoMaximo()) {
                throw new Exception("La clase " + clase.getNombre() + " ha alcanzado su cupo máximo.");
            }

            boolean yaAsignado = AsignarClasehelper.verificarClaseAsignadaACliente(cliente.getIdCliente(), clase.getIdClase());

            Membresia membresiaClase = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente(), "clase");
            boolean paseActivo = (membresiaClase != null && membresiaClase.getFechaVencimiento().isAfter(LocalDate.now()));

            if (paseActivo) {
                if (yaAsignado) {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Clase ya asignada", "El cliente ya está inscrito y su membresia está activa."));
                    return;
                } else {
                    AsignarClasehelper.asignarClaseACliente(cliente.getIdCliente(), clase.getIdClase());
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscrito con membresia", "El cliente tiene una membresia activa y ha sido inscrito."));
                    PrimeFaces.current().executeScript("PF('dlgPagoInteractivo2').hide();");
                }

            } else {
                if (montoIngresado == null || montoIngresado < 0) throw new Exception("Debe ingresar un monto.");
                calcularFaltante();
                if (montoIngresado < montoTotal) {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Monto insuficiente", "Faltan " + montoFaltante + " pesos."));
                    return;
                }
                montoCambio = montoIngresado - montoTotal;
                if (montoCambio < 0) montoCambio = 0.0;

                paga = new Paga();
                paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                paga.setFecha(LocalDate.now());
                paga.setIdCliente(cliente);
                paga.setMonto(montoTotal > 0 ? montoTotal : 500.00);
                paga.setPorPagar(porPagar);

                if (membresiaClase == null) {
                    nueva = new Membresia();
                    nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
                    nueva.setTipo("clase");
                    nueva.setIdCliente(cliente);
                    membresiaHelper.registrarMembresia(nueva, cliente);
                    pagaHelper.RealizarPago(paga, nueva.getIdItem());
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membresia creada", "Se ha creado la membresia de clase."));
                } else {
                    membresiaClase.setFechaVencimiento(LocalDate.now().plusDays(30));
                    membresiaHelper.modificarMembresia(membresiaClase);
                    pagaHelper.RealizarPago(paga, membresiaClase.getIdItem());                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membresia renovada", "Se ha renovado la membresia de clase."));
                }

                PrimeFaces.current().ajax().update("formPrincipal:dlgCambio2");
                PrimeFaces.current().executeScript("PF('dlgPagoInteractivo2').hide(); PF('dlgCambio2').show();");
            }

            if (!yaAsignado) {
                AsignarClasehelper.asignarClaseACliente(cliente.getIdCliente(), clase.getIdClase());
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Asignacion Exitosa", "Cliente inscrito en " + clase.getNombre() + "."));
            }

            montoIngresado = 0.0;
            montoFaltante = 0.0;
            fc.getExternalContext().getSessionMap().remove("idClase");
            fc.getExternalContext().getSessionMap().remove("idCliente");

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al pagar clase", e.getMessage()));
        }
    }

    public void realizarPagoTarjetaClase() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (usuarioRecepcionista == null) throw new Exception("Debe validar un recepcionista.");

            String idClase = (String) fc.getExternalContext().getSessionMap().get("idClase");
            String idClienteSesion = (String) fc.getExternalContext().getSessionMap().get("idCliente");
            if (idClase == null || idClienteSesion == null) throw new Exception("No se ha seleccionado una clase o cliente.");

            this.cliente = clienteHelper.obtenerCliente(idClienteSesion);
            if (cliente == null) throw new Exception("No se encontró el cliente con ID: " + idClienteSesion);

            Clase clase = claseHelper.obtenerClase(idClase);
            if (clase == null) throw new Exception("No se encontró la clase con ID: " + idClase);

            if (clase.getClientes().size() >= clase.getCupoMaximo()) {
                throw new Exception("La clase " + clase.getNombre() + " ha alcanzado su cupo máximo.");
            }

            boolean yaAsignado = AsignarClasehelper.verificarClaseAsignadaACliente(cliente.getIdCliente(), clase.getIdClase());

            Membresia membresiaClase = membresiaHelper.obtenerMembresiaPorCliente(cliente.getIdCliente(), "clase");
            boolean paseActivo = (membresiaClase != null && membresiaClase.getFechaVencimiento().isAfter(LocalDate.now()));

            if (paseActivo) {
                if (yaAsignado) {
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Clase ya asignada", "El cliente ya está inscrito y su membresia está activa."));
                    return;
                } else {
                    AsignarClasehelper.asignarClaseACliente(cliente.getIdCliente(), clase.getIdClase());
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscrito con membresia", "Se ha renovado la membresia de clase."));
                    PrimeFaces.current().executeScript("PF('dlgPagoTarjeta2').hide();");
                }
            } else {
                paga = new Paga();
                paga.setIdUsuariorecep(usuarioRecepcionista.getIdUsuariorecep());
                paga.setFecha(LocalDate.now());
                paga.setIdCliente(cliente);
                paga.setMonto(montoTotal > 0 ? montoTotal : 500.00);
                paga.setPorPagar(porPagar);

                if (membresiaClase == null) {
                    nueva = new Membresia();
                    nueva.setFechaVencimiento(LocalDate.now().plusDays(30));
                    nueva.setTipo("clase");
                    nueva.setIdCliente(cliente);
                    membresiaHelper.registrarMembresia(nueva, cliente);
                    pagaHelper.RealizarPago(paga, nueva.getIdItem());
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membresia creada", "Se ha creado la membresia de clase."));

                } else {
                    membresiaClase.setFechaVencimiento(LocalDate.now().plusDays(30));
                    membresiaHelper.modificarMembresia(membresiaClase);
                    pagaHelper.RealizarPago(paga, membresiaClase.getIdItem());
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Membresia renovada", "Se ha renovado la membresia de clase."));
                }

                PrimeFaces.current().executeScript("PF('dlgPagoTarjeta2').hide(); PF('dlgExitoTarjeta2').show();");
            }

            if (!yaAsignado) {
                AsignarClasehelper.asignarClaseACliente(cliente.getIdCliente(), clase.getIdClase());
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Éxito!", "Cliente inscrito en " + clase.getNombre() + "."));
            }

            if (!paseActivo) {
                limpiarCampos();
            }
            fc.getExternalContext().getSessionMap().remove("idClase");
            fc.getExternalContext().getSessionMap().remove("idCliente");

        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al pagar clase", e.getMessage()));
        }
    }

    public void prepararPago(String tipo) {

        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            // Obtiene el nombre del dialogo de pago
            String proximoDialogoWidgetVar = fc.getExternalContext().getRequestParameterMap().get("proximo");
            if (proximoDialogoWidgetVar == null || proximoDialogoWidgetVar.isEmpty()) {
                throw new Exception("Error interno: No se especificó el diálogo de pago.");
            }
            this.siguienteDialogo = proximoDialogoWidgetVar;

            // Reseta los valores
            this.creditoAplicado = 0.0;
            this.cliente = null;
            this.clienteTieneCredito = false;

            // Se scribio un id cliente
            if (this.idCliente != null && !this.idCliente.trim().isEmpty()) {

                // Si si se ecribio, ontiene el cleinte por su Id
                String idClienteParaBuscar = this.idCliente.trim();
                this.cliente = clienteHelper.obtenerCliente(idClienteParaBuscar);

                if (this.cliente == null) {
                    // El UR escribió un ID que no existe
                    throw new Exception("No se encontró el cliente con ID: " + idClienteParaBuscar);
                }

            } else {

                // Si viene de clases
                String idClienteDeClase = (String) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get("idCliente");

                if (idClienteDeClase != null && !idClienteDeClase.trim().isEmpty()) {
                    this.cliente = clienteHelper.obtenerCliente(idClienteDeClase);

                    if (this.cliente == null) {
                        throw new Exception("Error de sesión: El ID de cliente '" + idClienteDeClase + "' no se encontró en la BD.");
                    }

                } else {
                    // Si viene de membresias
                    Cliente clienteEnSesion = (Cliente) FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getSessionMap()
                            .get("clienteSeleccionado");

                    if (clienteEnSesion != null) {
                        Cliente clienteFresco = clienteHelper.obtenerCliente(clienteEnSesion.getIdCliente());

                        if (clienteFresco != null) {
                            this.cliente = clienteFresco;
                        } else {
                            this.cliente = clienteEnSesion;
                        }
                    }
                }
            }

            // Solo se revisa credito (si tenemos un cliente existente)
            if (this.cliente != null && this.cliente.getCredito() > 0.01) {
                this.clienteTieneCredito = true;
            }

            // Calcular total
            obtenerTotal(tipo);
            this.fecha = new Date();
            PrimeFaces.current().ajax().addCallbackParam("tieneCredito", this.clienteTieneCredito);

        } catch (Exception e) {
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al preparar pago", e.getMessage()));
        }
    }

    public void aplicarCredito() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            if (cliente == null || montoTotal == null || cliente.getCredito() == 0.00) {
                throw new Exception("No se puede aplicar el crédito. Faltan datos del cliente o del monto.");
            }

            double creditoDisponible = cliente.getCredito();
            if (creditoDisponible <= 0.01) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sin crédito", "El cliente no tiene crédito disponible."));
                return;
            }

            if (creditoDisponible >= montoTotal) {
                this.creditoAplicado = montoTotal;
                cliente.setCredito(creditoDisponible - montoTotal);
                montoTotal = 0.0;
            } else {
                this.creditoAplicado = creditoDisponible;
                montoTotal = montoTotal - creditoDisponible;
                cliente.setCredito(0.0);
            }

            clienteHelper.ModificarCliente(cliente);
            calcularFaltante();

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Crédito aplicado",
                    String.format("Se aplicaron $%.2f. Total a pagar: $%.2f", this.creditoAplicado, this.montoTotal)));

        } catch (Exception e) {
            fc.validationFailed();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al aplicar crédito", e.getMessage()));
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
        siguienteDialogo = null;
        clienteTieneCredito = false;
        creditoAplicado = 0.0;
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

    public String recargar() {
        return "pagos.xhtml?faces-redirect=true";
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

    public void setMontoIngresado(Double montoIngresado) { this.montoIngresado = montoIngresado; calcularFaltante(); }

    public Double getMontoCambio() { return montoCambio; }
    public void setMontoCambio(Double montoCambio) { this.montoCambio = montoCambio; }

    public Double getMontoFaltante() { return montoFaltante; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public String getSiguienteDialogo() { return siguienteDialogo; }
    public boolean isClienteTieneCredito() { return clienteTieneCredito; }
}