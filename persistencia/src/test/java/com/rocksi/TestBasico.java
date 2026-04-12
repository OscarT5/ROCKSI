//.
package com.rocksi;

import mx.desarollo.entity.Cliente;
import mx.desarollo.entity.Usuarioadministrador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestBasico {

    void validarRegistroCliente(Cliente c) throws Exception {
        if (c.getNombreCompleto() == null || c.getNombreCompleto().trim().isEmpty())
            throw new Exception("El nombre no puede estar vacio.");

        if (c.getTelefono() == null || c.getTelefono().trim().isEmpty())
            throw new Exception("El telefono no puede estar vacio.");

        if (!c.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new Exception("El nombre solo puede contener letras y espacios.");

        String normal = c.getTelefono().trim().replaceAll("[^0-9]", "");
        if (!normal.matches("\\d{7,15}"))
            throw new Exception("Telefono invalido. Debe contener entre 7 y 15 dígitos.");
    }

    void validarEliminarCliente(String idCliente) throws Exception {
        if (idCliente == null || idCliente.trim().isEmpty())
            throw new Exception("El id del cliente esta vacio");
    }

    void validarRegistroAdmin(Usuarioadministrador ua) throws Exception {
        if (ua.getNombreCompleto() == null || ua.getNombreCompleto().trim().isEmpty())
            throw new Exception("El nombre no puede estar vacio.");

        if (ua.getCorreo() == null || ua.getCorreo().trim().isEmpty())
            throw new Exception("El correo no puede estar vacio.");

        if (ua.getContrasena() == null || ua.getContrasena().trim().isEmpty())
            throw new Exception("Se debe asignar una contraseña.");

        if (!ua.getNombreCompleto().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
            throw new Exception("El nombre solo puede contener letras y espacios.");

        ua.setEstatus(1);
    }

    @Test
    @DisplayName("CP-U01a - Nombre vacio - excepcion")
    void testNombreVacio() {
        Cliente c = new Cliente();
        c.setNombreCompleto("");
        c.setTelefono("6641112233");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroCliente(c));
        assertEquals("El nombre no puede estar vacio.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U01b - Nombre null - excepcion")
    void testNombreNull() {
        Cliente c = new Cliente();
        c.setNombreCompleto(null);
        c.setTelefono("6641112233");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroCliente(c));
        assertEquals("El nombre no puede estar vacio.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U01c - Nombre con numeros - excepcion")
    void testNombreConNumeros() {
        Cliente c = new Cliente();
        c.setNombreCompleto("Juan123");
        c.setTelefono("6641112233");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroCliente(c));
        assertEquals("El nombre solo puede contener letras y espacios.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U01d - Nombre valido - sin excepcion")
    void testNombreValido() {
        Cliente c = new Cliente();
        c.setNombreCompleto("María López");
        c.setTelefono("6641112233");
        assertDoesNotThrow(() -> validarRegistroCliente(c));
    }

    @Test
    @DisplayName("CP-U02a - Telefono vacio - excepcion")
    void testTelefonoVacio() {
        Cliente c = new Cliente();
        c.setNombreCompleto("Juan Pérez");
        c.setTelefono("");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroCliente(c));
        assertEquals("El telefono no puede estar vacio.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U02b - Telefono menor a 7 digitos - excepcion")
    void testTelefonoCorto() {
        Cliente c = new Cliente();
        c.setNombreCompleto("Ana García");
        c.setTelefono("123");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroCliente(c));
        assertEquals("Telefono invalido. Debe contener entre 7 y 15 dígitos.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U02c - Telefono valido - sin excepcion")
    void testTelefonoValido() {
        Cliente c = new Cliente();
        c.setNombreCompleto("Carlos Ruiz");
        c.setTelefono("6641234567");
        assertDoesNotThrow(() -> validarRegistroCliente(c));
    }

    @Test
    @DisplayName("CP-U03a - ID null - excepcion")
    void testEliminarIdNull() {
        Exception ex = assertThrows(Exception.class, () -> validarEliminarCliente(null));
        assertEquals("El id del cliente esta vacio", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U03b - ID vacio - excepcion")
    void testEliminarIdVacio() {
        Exception ex = assertThrows(Exception.class, () -> validarEliminarCliente(""));
        assertEquals("El id del cliente esta vacio", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U03c - ID valido - sin excepcion")
    void testEliminarIdValido() {
        assertDoesNotThrow(() -> validarEliminarCliente("CLI1001"));
    }

    @Test
    @DisplayName("CP-U04a - Nombre admin vacio - excepcion")
    void testAdminNombreVacio() {
        Usuarioadministrador ua = new Usuarioadministrador();
        ua.setNombreCompleto("");
        ua.setCorreo("admin@rocksi.mx");
        ua.setContrasena("Pass123");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroAdmin(ua));
        assertEquals("El nombre no puede estar vacio.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U04b - Correo admin vacio - excepcion")
    void testAdminCorreoVacio() {
        Usuarioadministrador ua = new Usuarioadministrador();
        ua.setNombreCompleto("Admin Rocksi");
        ua.setCorreo("");
        ua.setContrasena("Pass123");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroAdmin(ua));
        assertEquals("El correo no puede estar vacio.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U04c - Contrasena admin vacia - excepcion")
    void testAdminContrasenaVacia() {
        Usuarioadministrador ua = new Usuarioadministrador();
        ua.setNombreCompleto("Admin Rocksi");
        ua.setCorreo("admin@rocksi.mx");
        ua.setContrasena("");
        Exception ex = assertThrows(Exception.class, () -> validarRegistroAdmin(ua));
        assertEquals("Se debe asignar una contraseña.", ex.getMessage());
    }

    @Test
    @DisplayName("CP-U04d - Datos validos - estatus 1")
    void testAdminDatosValidos() throws Exception {
        Usuarioadministrador ua = new Usuarioadministrador();
        ua.setNombreCompleto("Admin Rocksi");
        ua.setCorreo("admin@rocksi.mx");
        ua.setContrasena("Pass123");
        validarRegistroAdmin(ua);
        assertEquals(1, ua.getEstatus());
    }
}